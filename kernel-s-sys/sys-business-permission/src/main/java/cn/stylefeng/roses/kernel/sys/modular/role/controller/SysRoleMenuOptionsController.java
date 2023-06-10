package cn.stylefeng.roses.kernel.sys.modular.role.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleMenuOptionsRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色和菜单下的功能关联控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@RestController
@ApiResource(name = "角色和菜单下的功能关联")
public class SysRoleMenuOptionsController {

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @PostResource(name = "添加", path = "/sysRoleMenuOptions/add")
    public ResponseData<SysRoleMenuOptions> add(@RequestBody @Validated(SysRoleMenuOptionsRequest.add.class) SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        sysRoleMenuOptionsService.add(sysRoleMenuOptionsRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @PostResource(name = "删除", path = "/sysRoleMenuOptions/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysRoleMenuOptionsRequest.delete.class) SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        sysRoleMenuOptionsService.del(sysRoleMenuOptionsRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @PostResource(name = "编辑", path = "/sysRoleMenuOptions/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysRoleMenuOptionsRequest.edit.class) SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        sysRoleMenuOptionsService.edit(sysRoleMenuOptionsRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @GetResource(name = "查看详情", path = "/sysRoleMenuOptions/detail")
    public ResponseData<SysRoleMenuOptions> detail(@Validated(SysRoleMenuOptionsRequest.detail.class) SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        return new SuccessResponseData<>(sysRoleMenuOptionsService.detail(sysRoleMenuOptionsRequest));
    }

    /**
     * 获取列表
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @GetResource(name = "获取列表", path = "/sysRoleMenuOptions/list")
    public ResponseData<List<SysRoleMenuOptions>> list(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        return new SuccessResponseData<>(sysRoleMenuOptionsService.findList(sysRoleMenuOptionsRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @GetResource(name = "分页查询", path = "/sysRoleMenuOptions/page")
    public ResponseData<PageResult<SysRoleMenuOptions>> page(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        return new SuccessResponseData<>(sysRoleMenuOptionsService.findPage(sysRoleMenuOptionsRequest));
    }

}
