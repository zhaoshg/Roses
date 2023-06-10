package cn.stylefeng.roses.kernel.sys.modular.role.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleDataScope;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleDataScopeRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleDataScopeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色数据范围控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@RestController
@ApiResource(name = "角色数据范围")
public class SysRoleDataScopeController {

    @Resource
    private SysRoleDataScopeService sysRoleDataScopeService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @PostResource(name = "添加", path = "/sysRoleDataScope/add")
    public ResponseData<SysRoleDataScope> add(@RequestBody @Validated(SysRoleDataScopeRequest.add.class) SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        sysRoleDataScopeService.add(sysRoleDataScopeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @PostResource(name = "删除", path = "/sysRoleDataScope/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysRoleDataScopeRequest.delete.class) SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        sysRoleDataScopeService.del(sysRoleDataScopeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @PostResource(name = "编辑", path = "/sysRoleDataScope/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysRoleDataScopeRequest.edit.class) SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        sysRoleDataScopeService.edit(sysRoleDataScopeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @GetResource(name = "查看详情", path = "/sysRoleDataScope/detail")
    public ResponseData<SysRoleDataScope> detail(@Validated(SysRoleDataScopeRequest.detail.class) SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        return new SuccessResponseData<>(sysRoleDataScopeService.detail(sysRoleDataScopeRequest));
    }

    /**
     * 获取列表
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @GetResource(name = "获取列表", path = "/sysRoleDataScope/list")
    public ResponseData<List<SysRoleDataScope>> list(SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        return new SuccessResponseData<>(sysRoleDataScopeService.findList(sysRoleDataScopeRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    @GetResource(name = "分页查询", path = "/sysRoleDataScope/page")
    public ResponseData<PageResult<SysRoleDataScope>> page(SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        return new SuccessResponseData<>(sysRoleDataScopeService.findPage(sysRoleDataScopeRequest));
    }

}
