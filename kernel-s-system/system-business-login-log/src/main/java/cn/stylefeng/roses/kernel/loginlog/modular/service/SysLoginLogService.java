package cn.stylefeng.roses.kernel.loginlog.modular.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.loginlog.modular.entity.SysLoginLog;
import cn.stylefeng.roses.kernel.system.LoginLogServiceApi;
import cn.stylefeng.roses.kernel.system.pojo.SysLoginLogRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 登录日志service接口
 *
 * @author chenjinlong
 * @date 2021/1/13 10:56
 */
public interface SysLoginLogService extends IService<SysLoginLog>, LoginLogServiceApi {

    /**
     * 删除
     *
     * @param sysLoginLogRequest 参数
     * @author chenjinlong
     * @date 2021/1/13 10:55
     */
    void del(SysLoginLogRequest sysLoginLogRequest);

    /**
     * 查看相信
     *
     * @param sysLoginLogRequest 参数
     * @author chenjinlong
     * @date 2021/1/13 10:56
     */
    SysLoginLog detail(SysLoginLogRequest sysLoginLogRequest);

    /**
     * 分页查询
     *
     * @param sysLoginLogRequest 参数
     * @author chenjinlong
     * @date 2021/1/13 10:57
     */
    PageResult<SysLoginLog> findPage(SysLoginLogRequest sysLoginLogRequest);

}
