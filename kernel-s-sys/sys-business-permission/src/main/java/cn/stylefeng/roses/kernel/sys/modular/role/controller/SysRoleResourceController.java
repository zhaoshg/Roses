package cn.stylefeng.roses.kernel.sys.modular.role.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleResource;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleResourceRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleResourceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色资源关联控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@RestController
@ApiResource(name = "角色资源关联")
public class SysRoleResourceController {

    @Resource
    private SysRoleResourceService sysRoleResourceService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @PostResource(name = "添加", path = "/sysRoleResource/add")
    public ResponseData<SysRoleResource> add(@RequestBody @Validated(SysRoleResourceRequest.add.class) SysRoleResourceRequest sysRoleResourceRequest) {
        sysRoleResourceService.add(sysRoleResourceRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @PostResource(name = "删除", path = "/sysRoleResource/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysRoleResourceRequest.delete.class) SysRoleResourceRequest sysRoleResourceRequest) {
        sysRoleResourceService.del(sysRoleResourceRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @PostResource(name = "编辑", path = "/sysRoleResource/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysRoleResourceRequest.edit.class) SysRoleResourceRequest sysRoleResourceRequest) {
        sysRoleResourceService.edit(sysRoleResourceRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @GetResource(name = "查看详情", path = "/sysRoleResource/detail")
    public ResponseData<SysRoleResource> detail(@Validated(SysRoleResourceRequest.detail.class) SysRoleResourceRequest sysRoleResourceRequest) {
        return new SuccessResponseData<>(sysRoleResourceService.detail(sysRoleResourceRequest));
    }

    /**
     * 获取列表
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @GetResource(name = "获取列表", path = "/sysRoleResource/list")
    public ResponseData<List<SysRoleResource>> list(SysRoleResourceRequest sysRoleResourceRequest) {
        return new SuccessResponseData<>(sysRoleResourceService.findList(sysRoleResourceRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @GetResource(name = "分页查询", path = "/sysRoleResource/page")
    public ResponseData<PageResult<SysRoleResource>> page(SysRoleResourceRequest sysRoleResourceRequest) {
        return new SuccessResponseData<>(sysRoleResourceService.findPage(sysRoleResourceRequest));
    }

}
