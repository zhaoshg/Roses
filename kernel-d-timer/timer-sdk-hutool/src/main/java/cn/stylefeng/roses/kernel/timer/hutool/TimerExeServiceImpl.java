package cn.stylefeng.roses.kernel.timer.hutool;

import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.timer.api.TimerAction;
import cn.stylefeng.roses.kernel.timer.api.exception.TimerException;
import cn.stylefeng.roses.kernel.timer.api.exception.enums.TimerExceptionEnum;
import lombok.extern.slf4j.Slf4j;

/**
 *  class description: HuToolTimerExeServiceImpl的改造，
 *  当引入spring cloud后，需要对原有事件进行改造，使其只执行一次，防止出现错误。
 *  @author  xielin
 *  @date  2022/11/11 16:04
 */
@Slf4j
public class TimerExeServiceImpl extends HutoolTimerExeServiceImpl {
    @Override
    public void start() {
        CronUtil.setMatchSecond(true);
        if (!CronUtil.getScheduler().isStarted()) {
            CronUtil.start();
            log.info("scheduler{} is not started",CronUtil.getScheduler().toString());
        }else{
            log.info("scheduler{} has already started",CronUtil.getScheduler().toString());
        }
    }

    @Override
    public void startTimer(String taskId, String cron, String className, String params) {
        
        // 判断任务id是否为空
        if (StrUtil.isBlank(taskId)) {
            throw new TimerException(TimerExceptionEnum.PARAM_HAS_NULL, "taskId");
        }
    
        if (CronUtil.getScheduler().getTask(taskId)!=null){
            log.info("task ID:{} is existed，then will replace before one",taskId);
            CronUtil.getScheduler().deschedule(taskId);
        }
        
        // 判断任务cron表达式是否为空
        if (StrUtil.isBlank(cron)) {
            throw new TimerException(TimerExceptionEnum.PARAM_HAS_NULL, "cron");
        }

        // 判断类名称是否为空
        if (StrUtil.isBlank(className)) {
            throw new TimerException(TimerExceptionEnum.PARAM_HAS_NULL, "className");
        }

        // 预加载类看是否存在此定时任务类
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new TimerException(TimerExceptionEnum.CLASS_NOT_FOUND, className);
        }

        // 定义hutool的任务
        Task task = () -> {
            try {
                TimerAction timerAction = (TimerAction) SpringUtil.getBean(Class.forName(className));
                timerAction.action(params);
            } catch (ClassNotFoundException e) {
                log.error("任务执行异常：{}", e.getMessage());
            }
        };

        // 开始执行任务
        CronUtil.schedule(taskId, cron, task);
    }
}
