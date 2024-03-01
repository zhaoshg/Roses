package cn.stylefeng.roses.kernel.sys.modular.role.service;

import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionResponse;

import java.util.Set;

/**
 * 角色权限绑定业务
 *
 * @author fengshuonan
 * @since 2023/6/13 16:13
 */
public interface PermissionAssignService {

    /**
     * 获取角色绑定的权限列表
     * <p>
     * 角色绑定的权限列表返回的是一个树形结构：
     * 第一层是应用，第二层是应用下的菜单，第3层是菜单下的菜单功能
     *
     * @author fengshuonan
     * @since 2023/6/13 14:56
     */
    RoleBindPermissionResponse getRoleBindPermission(RoleBindPermissionRequest roleBindPermissionRequest);

    /**
     * 构建一个权限树，包含了空的选择状态
     * <p>
     * 包含所有的菜单和菜单功能的结构
     *
     * @author fengshuonan
     * @since 2023/6/13 16:17
     */
    RoleBindPermissionResponse createSelectTreeStructure();

    /**
     * 构建一个权限树，包含了空的选择状态
     * <p>
     * 包含指定范围内的菜单和菜单功能的结构
     *
     * @author fengshuonan
     * @since 2023/6/13 16:17
     */
    RoleBindPermissionResponse createSelectTreeStructure(Set<Long> limitMenuIdsAndOptionIds);

    /**
     * 获取角色绑定的菜单和菜单功能，直接返回菜单和菜单功能组成的id集合
     *
     * @author fengshuonan
     * @since 2023/6/13 18:11
     */
    Set<Long> getRoleBindMenusAndOptions(Long roleId);

    /**
     * 更新角色绑定菜单和菜单下的功能
     *
     * @author fengshuonan
     * @since 2023/6/13 19:48
     */
    void updateRoleBindPermission(RoleBindPermissionRequest roleBindPermissionRequest);

}
