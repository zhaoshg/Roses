package cn.stylefeng.roses.kernel.sys.modular.role.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRole;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleService;
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

    /**
     * 获取所有角色列表
     * <p>
     * 用在权限分配界面，左侧的角色列表
     *
     * @author fengshuonan
     * @since 2023/6/12 21:23
     */
    @GetResource(name = "获取所有角色列表", path = "/sysRole/getList")
    public ResponseData<List<SysRole>> getList(SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.findList(sysRoleRequest));
    }

}
