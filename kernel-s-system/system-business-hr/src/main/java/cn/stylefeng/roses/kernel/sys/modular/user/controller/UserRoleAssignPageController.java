package cn.stylefeng.roses.kernel.sys.modular.user.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.NewUserRoleBindResponse;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request.StatusControlRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysRoleAssignService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户授权界面接口
 *
 * @author fengshuonan
 * @since 2024/1/17 23:01
 */
@RestController
@ApiResource(name = "用户授权界面接口")
public class UserRoleAssignPageController {

    @Resource
    private SysRoleAssignService sysRoleAssignService;

    /**
     * 获取用户的角色授权信息列表
     *
     * @author fengshuonan
     * @since 2024/1/17 23:57
     */
    @GetResource(name = "获取用户的角色授权信息列表", path = "/sysRoleAssign/getUserAssignList")
    public ResponseData<List<NewUserRoleBindResponse>> getUserAssignList(@RequestParam("userId") Long userId) {
        List<NewUserRoleBindResponse> list = sysRoleAssignService.getUserAssignList(userId);
        return new SuccessResponseData<>(list);
    }

    /**
     * 修改用户针对某个公司的是否启用状态
     *
     * @author fengshuonan
     * @since 2024-01-18 9:31
     */
    @PostResource(name = "修改用户针对某个公司的是否启用状态", path = "/sysRoleAssign/changeStatus")
    public ResponseData<?> changeStatus(@RequestBody @Validated StatusControlRequest statusControlRequest) {
        sysRoleAssignService.changeUserBindStatus(statusControlRequest);
        return new SuccessResponseData<>();
    }

}
