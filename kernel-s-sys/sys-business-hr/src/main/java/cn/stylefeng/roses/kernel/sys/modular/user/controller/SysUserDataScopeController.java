package cn.stylefeng.roses.kernel.sys.modular.user.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserDataScope;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserDataScopeRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserDataScopeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户数据范围控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@RestController
@ApiResource(name = "用户数据范围")
public class SysUserDataScopeController {

    @Resource
    private SysUserDataScopeService sysUserDataScopeService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @PostResource(name = "添加", path = "/sysUserDataScope/add")
    public ResponseData<SysUserDataScope> add(@RequestBody @Validated(SysUserDataScopeRequest.add.class) SysUserDataScopeRequest sysUserDataScopeRequest) {
        sysUserDataScopeService.add(sysUserDataScopeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @PostResource(name = "删除", path = "/sysUserDataScope/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysUserDataScopeRequest.delete.class) SysUserDataScopeRequest sysUserDataScopeRequest) {
        sysUserDataScopeService.del(sysUserDataScopeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @PostResource(name = "编辑", path = "/sysUserDataScope/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysUserDataScopeRequest.edit.class) SysUserDataScopeRequest sysUserDataScopeRequest) {
        sysUserDataScopeService.edit(sysUserDataScopeRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @GetResource(name = "查看详情", path = "/sysUserDataScope/detail")
    public ResponseData<SysUserDataScope> detail(@Validated(SysUserDataScopeRequest.detail.class) SysUserDataScopeRequest sysUserDataScopeRequest) {
        return new SuccessResponseData<>(sysUserDataScopeService.detail(sysUserDataScopeRequest));
    }

    /**
     * 获取列表
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @GetResource(name = "获取列表", path = "/sysUserDataScope/list")
    public ResponseData<List<SysUserDataScope>> list(SysUserDataScopeRequest sysUserDataScopeRequest) {
        return new SuccessResponseData<>(sysUserDataScopeService.findList(sysUserDataScopeRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @GetResource(name = "分页查询", path = "/sysUserDataScope/page")
    public ResponseData<PageResult<SysUserDataScope>> page(SysUserDataScopeRequest sysUserDataScopeRequest) {
        return new SuccessResponseData<>(sysUserDataScopeService.findPage(sysUserDataScopeRequest));
    }

}
