package cn.stylefeng.roses.kernel.sys.modular.menu.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuResource;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuResourceRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuResourceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单资源绑定控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@RestController
@ApiResource(name = "菜单资源绑定")
public class SysMenuResourceController {

    @Resource
    private SysMenuResourceService sysMenuResourceService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "添加", path = "/sysMenuResource/add")
    public ResponseData<SysMenuResource> add(@RequestBody @Validated(SysMenuResourceRequest.add.class) SysMenuResourceRequest sysMenuResourceRequest) {
        sysMenuResourceService.add(sysMenuResourceRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "删除", path = "/sysMenuResource/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysMenuResourceRequest.delete.class) SysMenuResourceRequest sysMenuResourceRequest) {
        sysMenuResourceService.del(sysMenuResourceRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "编辑", path = "/sysMenuResource/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysMenuResourceRequest.edit.class) SysMenuResourceRequest sysMenuResourceRequest) {
        sysMenuResourceService.edit(sysMenuResourceRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @GetResource(name = "查看详情", path = "/sysMenuResource/detail")
    public ResponseData<SysMenuResource> detail(@Validated(SysMenuResourceRequest.detail.class) SysMenuResourceRequest sysMenuResourceRequest) {
        return new SuccessResponseData<>(sysMenuResourceService.detail(sysMenuResourceRequest));
    }

    /**
     * 获取列表
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @GetResource(name = "获取列表", path = "/sysMenuResource/list")
    public ResponseData<List<SysMenuResource>> list(SysMenuResourceRequest sysMenuResourceRequest) {
        return new SuccessResponseData<>(sysMenuResourceService.findList(sysMenuResourceRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @GetResource(name = "分页查询", path = "/sysMenuResource/page")
    public ResponseData<PageResult<SysMenuResource>> page(SysMenuResourceRequest sysMenuResourceRequest) {
        return new SuccessResponseData<>(sysMenuResourceService.findPage(sysMenuResourceRequest));
    }

}
