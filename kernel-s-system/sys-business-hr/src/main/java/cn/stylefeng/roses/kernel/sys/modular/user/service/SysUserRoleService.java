package cn.stylefeng.roses.kernel.sys.modular.user.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.api.SysUserRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserRole;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRoleRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户角色关联 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
public interface SysUserRoleService extends IService<SysUserRole>, SysUserRoleServiceApi {

    /**
     * 新增
     *
     * @param sysUserRoleRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    void add(SysUserRoleRequest sysUserRoleRequest);

    /**
     * 删除
     *
     * @param sysUserRoleRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    void del(SysUserRoleRequest sysUserRoleRequest);

    /**
     * 编辑
     *
     * @param sysUserRoleRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    void edit(SysUserRoleRequest sysUserRoleRequest);

    /**
     * 查询详情
     *
     * @param sysUserRoleRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    SysUserRole detail(SysUserRoleRequest sysUserRoleRequest);

    /**
     * 获取列表
     *
     * @param sysUserRoleRequest 请求参数
     * @return List<SysUserRole>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    List<SysUserRole> findList(SysUserRoleRequest sysUserRoleRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysUserRoleRequest 请求参数
     * @return PageResult<SysUserRole>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    PageResult<SysUserRole> findPage(SysUserRoleRequest sysUserRoleRequest);

    /**
     * 绑定用户角色
     *
     * @author fengshuonan
     * @since 2023/6/12 13:47
     */
    void bindRoles(SysUserRoleRequest sysUserRoleRequest);

    /**
     * 给用户添加默认的角色
     *
     * @author fengshuonan
     * @since 2023/6/25 0:34
     */
    void bindUserDefaultRole(Long userId);

}