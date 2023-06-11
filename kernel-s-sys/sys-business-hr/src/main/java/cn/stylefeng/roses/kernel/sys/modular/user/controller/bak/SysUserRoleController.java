package cn.stylefeng.roses.kernel.sys.modular.user.controller.bak;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserRole;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRoleRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserRoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户角色关联控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@RestController
@ApiResource(name = "用户角色关联")
public class SysUserRoleController {

    @Resource
    private SysUserRoleService sysUserRoleService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @PostResource(name = "添加", path = "/sysUserRole/add")
    public ResponseData<SysUserRole> add(@RequestBody @Validated(SysUserRoleRequest.add.class) SysUserRoleRequest sysUserRoleRequest) {
        sysUserRoleService.add(sysUserRoleRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @PostResource(name = "删除", path = "/sysUserRole/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysUserRoleRequest.delete.class) SysUserRoleRequest sysUserRoleRequest) {
        sysUserRoleService.del(sysUserRoleRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @PostResource(name = "编辑", path = "/sysUserRole/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysUserRoleRequest.edit.class) SysUserRoleRequest sysUserRoleRequest) {
        sysUserRoleService.edit(sysUserRoleRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @GetResource(name = "查看详情", path = "/sysUserRole/detail")
    public ResponseData<SysUserRole> detail(@Validated(SysUserRoleRequest.detail.class) SysUserRoleRequest sysUserRoleRequest) {
        return new SuccessResponseData<>(sysUserRoleService.detail(sysUserRoleRequest));
    }

    /**
     * 获取列表
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @GetResource(name = "获取列表", path = "/sysUserRole/list")
    public ResponseData<List<SysUserRole>> list(SysUserRoleRequest sysUserRoleRequest) {
        return new SuccessResponseData<>(sysUserRoleService.findList(sysUserRoleRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @GetResource(name = "分页查询", path = "/sysUserRole/page")
    public ResponseData<PageResult<SysUserRole>> page(SysUserRoleRequest sysUserRoleRequest) {
        return new SuccessResponseData<>(sysUserRoleService.findPage(sysUserRoleRequest));
    }

}
