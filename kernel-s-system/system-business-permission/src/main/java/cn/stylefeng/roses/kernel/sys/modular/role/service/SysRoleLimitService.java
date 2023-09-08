package cn.stylefeng.roses.kernel.sys.modular.role.service;

import cn.stylefeng.roses.kernel.sys.api.SysRoleLimitServiceApi;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleLimit;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 角色权限限制 服务类
 *
 * @author fengshuonan
 * @date 2023/09/08 12:55
 */
public interface SysRoleLimitService extends IService<SysRoleLimit>, SysRoleLimitServiceApi {

    /**
     * 获取角色绑定的权限限制列表
     *
     * @author fengshuonan
     * @since 2023/9/8 13:50
     */
    RoleBindPermissionResponse getRoleLimit(RoleBindPermissionRequest roleBindPermissionRequest);

    /**
     * 更新角色下绑定的权限限制
     *
     * @author fengshuonan
     * @since 2023/9/8 14:06
     */
    void updateRoleBindLimit(RoleBindPermissionRequest roleBindPermissionRequest);

}