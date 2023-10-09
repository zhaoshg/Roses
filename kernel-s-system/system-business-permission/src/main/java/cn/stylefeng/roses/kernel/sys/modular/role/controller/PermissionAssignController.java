package cn.stylefeng.roses.kernel.sys.modular.role.controller;

import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.api.constants.PermissionCodeConstants;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRole;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindDataScopeRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindDataScopeResponse;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionResponse;
import cn.stylefeng.roses.kernel.sys.modular.role.service.PermissionAssignService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleDataScopeService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限分配界面的接口
 *
 * @author fengshuonan
 * @since 2023/6/12 21:22
 */
@RestController
@ApiResource(name = "权限分配界面的接口")
public class PermissionAssignController {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private PermissionAssignService permissionAssignService;

    @Resource
    private SysRoleDataScopeService sysRoleDataScopeService;

    /**
     * 获取所有角色列表
     * <p>
     * 用在权限分配界面，左侧的角色列表
     *
     * @author fengshuonan
     * @since 2023/6/12 21:23
     */
    @GetResource(name = "获取所有角色列表", path = "/permission/getRoleList")
    public ResponseData<List<SysRole>> getRoleList(SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.findList(sysRoleRequest));
    }

    /**
     * 获取角色绑定的权限列表
     * <p>
     * 角色绑定的权限列表返回的是一个树形结构：
     * 第一层是应用，第二层是应用下的菜单，第3层是菜单下的菜单功能
     *
     * @author fengshuonan
     * @since 2023/6/12 21:23
     */
    @GetResource(name = "获取角色绑定的权限列表", path = "/permission/getRoleBindPermission")
    public ResponseData<RoleBindPermissionResponse> getRoleBindPermission(
            @Validated(BaseRequest.detail.class) RoleBindPermissionRequest roleBindPermissionRequest) {
        RoleBindPermissionResponse roleBindPermission = permissionAssignService.getRoleBindPermission(roleBindPermissionRequest);
        return new SuccessResponseData<>(roleBindPermission);
    }

    /**
     * 更新角色绑定权限
     * <p>
     * 每点击一个权限直接调用一次接口，实时保存
     *
     * @author fengshuonan
     * @since 2023/6/13 19:45
     */
    @PostResource(name = "更新角色绑定权限", path = "/permission/updateRoleBindPermission", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.CHANGE_ROLE_PERMISSION)
    public ResponseData<?> updateRoleBindPermission(@RequestBody @Validated(RoleBindPermissionRequest.roleBindPermission.class)
                                                    RoleBindPermissionRequest roleBindPermissionRequest) {
        permissionAssignService.updateRoleBindPermission(roleBindPermissionRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 获取角色的数据权限详情
     *
     * @author fengshuonan
     * @since 2023/7/16 23:15
     */
    @GetResource(name = "获取角色的数据权限详情", path = "/permission/getRoleBindDataScope")
    public ResponseData<RoleBindDataScopeResponse> getRoleBindDataScope(
            @Validated(BaseRequest.detail.class) RoleBindDataScopeRequest roleBindDataScopeRequest) {
        RoleBindDataScopeResponse roleBindDataScopeResponse = sysRoleDataScopeService.getRoleBindDataScope(roleBindDataScopeRequest);
        return new SuccessResponseData<>(roleBindDataScopeResponse);
    }

    /**
     * 角色绑定数据权限的配置
     *
     * @author fengshuonan
     * @since 2023/7/16 23:40
     */
    @PostResource(name = "角色绑定数据权限的配置", path = "/permission/updateRoleBindDataScope", requiredPermission = true,
            requirePermissionCode = PermissionCodeConstants.CHANGE_ROLE_DATA_SCOPE)
    public ResponseData<?> updateRoleBindDataScope(
            @RequestBody @Validated(RoleBindDataScopeRequest.roleBindDataScope.class) RoleBindDataScopeRequest roleBindDataScopeRequest) {
        sysRoleDataScopeService.updateRoleBindDataScope(roleBindDataScopeRequest);
        return new SuccessResponseData<>();
    }

}
