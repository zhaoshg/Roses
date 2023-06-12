package cn.stylefeng.roses.kernel.sys.modular.user.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUser;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRoleRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserRoleService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统用户控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@RestController
@ApiResource(name = "系统用户")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    /**
     * 添加用户
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @PostResource(name = "添加用户", path = "/sysUser/add")
    public ResponseData<SysUser> add(@RequestBody @Validated(SysUserRequest.add.class) SysUserRequest sysUserRequest) {
        sysUserService.add(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除用户
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @PostResource(name = "删除用户", path = "/sysUser/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysUserRequest.delete.class) SysUserRequest sysUserRequest) {
        sysUserService.del(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 批量删除用户
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @PostResource(name = "批量删除用户", path = "/sysUser/batchDelete")
    public ResponseData<?> batchDelete(@RequestBody @Validated(BaseRequest.batchDelete.class) SysUserRequest sysUserRequest) {
        sysUserService.batchDel(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑用户
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @PostResource(name = "编辑用户", path = "/sysUser/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysUserRequest.edit.class) SysUserRequest sysUserRequest) {
        sysUserService.edit(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看用户详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @GetResource(name = "查看用户详情", path = "/sysUser/detail")
    public ResponseData<SysUser> detail(@Validated(SysUserRequest.detail.class) SysUserRequest sysUserRequest) {
        return new SuccessResponseData<>(sysUserService.detail(sysUserRequest));
    }

    /**
     * 获取列表-用户信息（带分页）
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @GetResource(name = "分页查询-用户信息", path = "/sysUser/page")
    public ResponseData<PageResult<SysUser>> page(SysUserRequest sysUserRequest) {
        return new SuccessResponseData<>(sysUserService.findPage(sysUserRequest));
    }

    /**
     * 修改用户状态
     *
     * @author fengshuonan
     * @since 2023/6/12 10:58
     */
    @PostResource(name = "修改用户状态", path = "/sysUser/updateStatus")
    public ResponseData<?> updateStatus(@RequestBody @Validated(SysUserRequest.updateStatus.class) SysUserRequest sysUserRequest) {
        sysUserService.updateStatus(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 绑定用户角色
     *
     * @author fengshuonan
     * @since 2023/6/12 10:58
     */
    @PostResource(name = "绑定用户角色", path = "/sysUser/bindRoles")
    public ResponseData<?> bindRoles(@RequestBody @Validated(SysUserRoleRequest.bindRoles.class) SysUserRoleRequest sysUserRoleRequest) {
        sysUserRoleService.bindRoles(sysUserRoleRequest);
        return new SuccessResponseData<>();
    }

}
