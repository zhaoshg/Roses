package cn.stylefeng.roses.kernel.sys.modular.user.service;

import cn.stylefeng.roses.kernel.sys.api.SysUserRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserRole;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRoleRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户角色关联 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
public interface SysUserRoleService extends IService<SysUserRole>, SysUserRoleServiceApi {

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

    /**
     * 获取用户是否绑定的对应的角色
     *
     * @author fengshuonan
     * @since 2024-01-18 10:54
     */
    SysUserRole getPointUserRole(Long userId, Long roleId, Long orgId);

    /**
     * 新增一个角色绑定
     *
     * @author fengshuonan
     * @since 2024-01-18 11:00
     */
    void addBusinessAndCompanyBindRole(SysUserRole sysUserRole);

}