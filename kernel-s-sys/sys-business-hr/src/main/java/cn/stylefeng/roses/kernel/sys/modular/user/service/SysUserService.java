package cn.stylefeng.roses.kernel.sys.modular.user.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUser;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统用户 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 新增
     *
     * @param sysUserRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    void add(SysUserRequest sysUserRequest);

    /**
     * 删除
     *
     * @param sysUserRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    void del(SysUserRequest sysUserRequest);

    /**
     * 批量删除用户
     *
     * @author fengshuonan
     * @since 2023/6/12 10:41
     */
    void batchDel(SysUserRequest sysUserRequest);

    /**
     * 编辑
     *
     * @param sysUserRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    void edit(SysUserRequest sysUserRequest);

    /**
     * 查询详情
     *
     * @param sysUserRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    SysUser detail(SysUserRequest sysUserRequest);

    /**
     * 获取列表
     *
     * @param sysUserRequest 请求参数
     * @return List<SysUser>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    List<SysUser> findList(SysUserRequest sysUserRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysUserRequest 请求参数
     * @return PageResult<SysUser>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    PageResult<SysUser> findPage(SysUserRequest sysUserRequest);
}