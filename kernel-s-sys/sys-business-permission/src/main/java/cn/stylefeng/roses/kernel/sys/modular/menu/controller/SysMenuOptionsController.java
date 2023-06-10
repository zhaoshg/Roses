package cn.stylefeng.roses.kernel.sys.modular.menu.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuOptionsRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单下的功能操作控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@RestController
@ApiResource(name = "菜单下的功能操作")
public class SysMenuOptionsController {

    @Resource
    private SysMenuOptionsService sysMenuOptionsService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "添加", path = "/sysMenuOptions/add")
    public ResponseData<SysMenuOptions> add(@RequestBody @Validated(SysMenuOptionsRequest.add.class) SysMenuOptionsRequest sysMenuOptionsRequest) {
        sysMenuOptionsService.add(sysMenuOptionsRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "删除", path = "/sysMenuOptions/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysMenuOptionsRequest.delete.class) SysMenuOptionsRequest sysMenuOptionsRequest) {
        sysMenuOptionsService.del(sysMenuOptionsRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "编辑", path = "/sysMenuOptions/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysMenuOptionsRequest.edit.class) SysMenuOptionsRequest sysMenuOptionsRequest) {
        sysMenuOptionsService.edit(sysMenuOptionsRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @GetResource(name = "查看详情", path = "/sysMenuOptions/detail")
    public ResponseData<SysMenuOptions> detail(@Validated(SysMenuOptionsRequest.detail.class) SysMenuOptionsRequest sysMenuOptionsRequest) {
        return new SuccessResponseData<>(sysMenuOptionsService.detail(sysMenuOptionsRequest));
    }

    /**
     * 获取列表
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @GetResource(name = "获取列表", path = "/sysMenuOptions/list")
    public ResponseData<List<SysMenuOptions>> list(SysMenuOptionsRequest sysMenuOptionsRequest) {
        return new SuccessResponseData<>(sysMenuOptionsService.findList(sysMenuOptionsRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @GetResource(name = "分页查询", path = "/sysMenuOptions/page")
    public ResponseData<PageResult<SysMenuOptions>> page(SysMenuOptionsRequest sysMenuOptionsRequest) {
        return new SuccessResponseData<>(sysMenuOptionsService.findPage(sysMenuOptionsRequest));
    }

}
