package cn.stylefeng.roses.kernel.sys.modular.role.controller;

import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRole;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionResponse;
import cn.stylefeng.roses.kernel.sys.modular.role.service.PermissionAssignService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleService;
import org.springframework.validation.annotation.Validated;
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
    public ResponseData<RoleBindPermissionResponse> getRoleBindPermission(@Validated(BaseRequest.detail.class) RoleBindPermissionRequest roleBindPermissionRequest) {
        RoleBindPermissionResponse roleBindPermission = permissionAssignService.getRoleBindPermission(roleBindPermissionRequest);
        return new SuccessResponseData<>(roleBindPermission);
    }


}
