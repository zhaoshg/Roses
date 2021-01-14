package cn.stylefeng.roses.kernel.log.db;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.LogManagerApi;
import cn.stylefeng.roses.kernel.log.api.pojo.manage.LogManagerParam;
import cn.stylefeng.roses.kernel.log.api.pojo.record.LogRecordDTO;
import cn.stylefeng.roses.kernel.log.db.entity.SysLog;
import cn.stylefeng.roses.kernel.log.db.service.SysLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志管理，数据库实现
 *
 * @author luojie
 * @date 2020/11/2 17:40
 */
@Slf4j
public class DbLogManagerServiceImpl implements LogManagerApi {

    /**
     * 日志记录 service接口
     */
    @Resource
    private SysLogService sysLogService;

    @Override
    public List<LogRecordDTO> queryLogList(LogManagerParam logManagerParam) {
        PageResult<LogRecordDTO> logRecordDtoPageResult = queryLogListPage(logManagerParam);
        return logRecordDtoPageResult.getRows();
    }

    @Override
    public PageResult<LogRecordDTO> queryLogListPage(LogManagerParam logManagerParam) {
        if (ObjectUtil.isEmpty(logManagerParam)) {
            return new PageResult<>();
        }

        LambdaQueryWrapper<SysLog> sysLogLambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 创建查询条件
        createQueryCondition(logManagerParam, sysLogLambdaQueryWrapper);

        // 查询分页结果
        Page<SysLog> objectPage = PageFactory.defaultPage();
        Page<SysLog> page = sysLogService.page(objectPage, sysLogLambdaQueryWrapper);

        PageResult<SysLog> pageResult = PageResultFactory.createPageResult(page);
        PageResult<LogRecordDTO> logRecordDtoPageResult = new PageResult<>();
        BeanUtil.copyProperties(pageResult, logRecordDtoPageResult);

        return logRecordDtoPageResult;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void deleteLogs(LogManagerParam logManagerParam) {
        if (ObjectUtil.isEmpty(logManagerParam)) {
            return;
        }

        // 获取参数
        String beginDateTime = logManagerParam.getBeginDateTime();
        String endDateTime = logManagerParam.getEndDateTime();
        String appName = logManagerParam.getAppName();

        LogManagerParam param = new LogManagerParam();
        param.setBeginDateTime(beginDateTime);
        param.setEndDateTime(endDateTime);
        param.setAppName(appName);

        LambdaQueryWrapper<SysLog> sysLogLambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 创建查询条件
        createQueryCondition(param, sysLogLambdaQueryWrapper);

        // 删除指定条件日志
        sysLogService.remove(sysLogLambdaQueryWrapper);
    }

    /**
     * 创建查询条件
     *
     * @param logManagerParam          日志管理的查询参数
     * @param sysLogLambdaQueryWrapper mp的查询条件
     * @author luojie
     * @date 2020/11/3 11:22
     */
    private void createQueryCondition(LogManagerParam logManagerParam, LambdaQueryWrapper<SysLog> sysLogLambdaQueryWrapper) {

        // 设置查询条件的起始时间和结束时间
        if (StrUtil.isNotBlank(logManagerParam.getBeginDateTime()) && StrUtil.isNotBlank(logManagerParam.getEndDateTime())) {
            sysLogLambdaQueryWrapper.between(SysLog::getCreateTime, logManagerParam.getBeginDateTime(), logManagerParam.getEndDateTime());
        }

        // 根据日志名称查询
        String name = logManagerParam.getLogName();
        if (StrUtil.isNotEmpty(name)) {
            sysLogLambdaQueryWrapper.and(q -> {
                q.eq(SysLog::getLogName, name);
            });
        }

        // 根据服务名称查询
        String appName = logManagerParam.getAppName();
        if (StrUtil.isNotEmpty(appName)) {
            sysLogLambdaQueryWrapper.and(q -> {
                q.eq(SysLog::getAppName, appName);
            });
        }

        // 根据服务端ip查询
        String serverIp = logManagerParam.getServerIp();
        if (StrUtil.isNotEmpty(serverIp)) {
            sysLogLambdaQueryWrapper.and(q -> {
                q.eq(SysLog::getServerIp, serverIp);
            });
        }

        // 根据客户端请求的用户id查询
        Long userId = logManagerParam.getUserId();
        if (userId != null) {
            sysLogLambdaQueryWrapper.and(q -> {
                q.eq(SysLog::getUserId, userId);
            });
        }

        // 根据客户端的ip查询
        String clientIp = logManagerParam.getClientIp();
        if (StrUtil.isNotEmpty(clientIp)) {
            sysLogLambdaQueryWrapper.and(q -> {
                q.eq(SysLog::getClientIp, clientIp);
            });
        }

        // 根据当前用户请求的url查询
        String url = logManagerParam.getRequestUrl();
        if (StrUtil.isNotEmpty(clientIp)) {
            sysLogLambdaQueryWrapper.and(q -> {
                q.like(SysLog::getRequestUrl, url);
            });
        }

        // 查询特定字段
        sysLogLambdaQueryWrapper.select(
                SysLog::getLogId,
                SysLog::getLogName,
                SysLog::getLogContent,
                SysLog::getAppName,
                SysLog::getRequestUrl,
                SysLog::getServerIp,
                SysLog::getClientIp,
                SysLog::getUserId,
                SysLog::getHttpMethod,
                SysLog::getClientBrowser,
                SysLog::getClientOs,
                BaseEntity::getCreateTime,
                BaseEntity::getUpdateTime);

        // 根据时间倒序排序
        sysLogLambdaQueryWrapper.orderByDesc(SysLog::getCreateTime);
    }

}
