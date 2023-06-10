package cn.stylefeng.roses.kernel.sys.modular.role.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleMenuRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色菜单关联控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@RestController
@ApiResource(name = "角色菜单关联")
public class SysRoleMenuController {

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @PostResource(name = "添加", path = "/sysRoleMenu/add")
    public ResponseData<SysRoleMenu> add(@RequestBody @Validated(SysRoleMenuRequest.add.class) SysRoleMenuRequest sysRoleMenuRequest) {
        sysRoleMenuService.add(sysRoleMenuRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @PostResource(name = "删除", path = "/sysRoleMenu/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysRoleMenuRequest.delete.class) SysRoleMenuRequest sysRoleMenuRequest) {
        sysRoleMenuService.del(sysRoleMenuRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @PostResource(name = "编辑", path = "/sysRoleMenu/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysRoleMenuRequest.edit.class) SysRoleMenuRequest sysRoleMenuRequest) {
        sysRoleMenuService.edit(sysRoleMenuRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @GetResource(name = "查看详情", path = "/sysRoleMenu/detail")
    public ResponseData<SysRoleMenu> detail(@Validated(SysRoleMenuRequest.detail.class) SysRoleMenuRequest sysRoleMenuRequest) {
        return new SuccessResponseData<>(sysRoleMenuService.detail(sysRoleMenuRequest));
    }

    /**
     * 获取列表
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @GetResource(name = "获取列表", path = "/sysRoleMenu/list")
    public ResponseData<List<SysRoleMenu>> list(SysRoleMenuRequest sysRoleMenuRequest) {
        return new SuccessResponseData<>(sysRoleMenuService.findList(sysRoleMenuRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @GetResource(name = "分页查询", path = "/sysRoleMenu/page")
    public ResponseData<PageResult<SysRoleMenu>> page(SysRoleMenuRequest sysRoleMenuRequest) {
        return new SuccessResponseData<>(sysRoleMenuService.findPage(sysRoleMenuRequest));
    }

}
