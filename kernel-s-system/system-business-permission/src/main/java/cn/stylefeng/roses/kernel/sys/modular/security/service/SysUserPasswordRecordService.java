package cn.stylefeng.roses.kernel.sys.modular.security.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.security.entity.SysUserPasswordRecord;
import cn.stylefeng.roses.kernel.sys.modular.security.pojo.request.SysUserPasswordRecordRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户历史密码记录服务类
 *
 * @author fengshuonan
 * @date 2023/10/04 23:28
 */
public interface SysUserPasswordRecordService extends IService<SysUserPasswordRecord> {

    /**
     * 新增用户历史密码记录
     *
     * @param sysUserPasswordRecordRequest 请求参数
     * @author fengshuonan
     * @date 2023/10/04 23:28
     */
    void add(SysUserPasswordRecordRequest sysUserPasswordRecordRequest);

    /**
     * 删除用户历史密码记录
     *
     * @param sysUserPasswordRecordRequest 请求参数
     * @author fengshuonan
     * @date 2023/10/04 23:28
     */
    void del(SysUserPasswordRecordRequest sysUserPasswordRecordRequest);

    /**
     * 编辑用户历史密码记录
     *
     * @param sysUserPasswordRecordRequest 请求参数
     * @author fengshuonan
     * @date 2023/10/04 23:28
     */
    void edit(SysUserPasswordRecordRequest sysUserPasswordRecordRequest);

    /**
     * 查询详情用户历史密码记录
     *
     * @param sysUserPasswordRecordRequest 请求参数
     * @author fengshuonan
     * @date 2023/10/04 23:28
     */
    SysUserPasswordRecord detail(SysUserPasswordRecordRequest sysUserPasswordRecordRequest);

    /**
     * 获取用户历史密码记录列表
     *
     * @param sysUserPasswordRecordRequest 请求参数
     * @return List<SysUserPasswordRecord>  返回结果
     * @author fengshuonan
     * @date 2023/10/04 23:28
     */
    List<SysUserPasswordRecord> findList(SysUserPasswordRecordRequest sysUserPasswordRecordRequest);

    /**
     * 获取用户历史密码记录分页列表
     *
     * @param sysUserPasswordRecordRequest 请求参数
     * @return PageResult<SysUserPasswordRecord>   返回结果
     * @author fengshuonan
     * @date 2023/10/04 23:28
     */
    PageResult<SysUserPasswordRecord> findPage(SysUserPasswordRecordRequest sysUserPasswordRecordRequest);

}
