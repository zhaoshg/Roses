package cn.stylefeng.roses.kernel.sys.modular.menu.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统菜单控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@RestController
@ApiResource(name = "系统菜单")
public class SysMenuController {

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "添加", path = "/sysMenu/add")
    public ResponseData<SysMenu> add(@RequestBody @Validated(SysMenuRequest.add.class) SysMenuRequest sysMenuRequest) {
        sysMenuService.add(sysMenuRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "删除", path = "/sysMenu/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysMenuRequest.delete.class) SysMenuRequest sysMenuRequest) {
        sysMenuService.del(sysMenuRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "编辑", path = "/sysMenu/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysMenuRequest.edit.class) SysMenuRequest sysMenuRequest) {
        sysMenuService.edit(sysMenuRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @GetResource(name = "查看详情", path = "/sysMenu/detail")
    public ResponseData<SysMenu> detail(@Validated(SysMenuRequest.detail.class) SysMenuRequest sysMenuRequest) {
        return new SuccessResponseData<>(sysMenuService.detail(sysMenuRequest));
    }

    /**
     * 获取列表
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @GetResource(name = "获取列表", path = "/sysMenu/list")
    public ResponseData<List<SysMenu>> list(SysMenuRequest sysMenuRequest) {
        return new SuccessResponseData<>(sysMenuService.findList(sysMenuRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @GetResource(name = "分页查询", path = "/sysMenu/page")
    public ResponseData<PageResult<SysMenu>> page(SysMenuRequest sysMenuRequest) {
        return new SuccessResponseData<>(sysMenuService.findPage(sysMenuRequest));
    }

}
