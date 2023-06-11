package cn.stylefeng.roses.kernel.sys.modular.user.controller.bak;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserOrg;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserOrgRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserOrgService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户组织机构关联控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@RestController
@ApiResource(name = "用户组织机构关联")
public class SysUserOrgController {

    @Resource
    private SysUserOrgService sysUserOrgService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @PostResource(name = "添加", path = "/sysUserOrg/add")
    public ResponseData<SysUserOrg> add(@RequestBody @Validated(SysUserOrgRequest.add.class) SysUserOrgRequest sysUserOrgRequest) {
        sysUserOrgService.add(sysUserOrgRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @PostResource(name = "删除", path = "/sysUserOrg/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysUserOrgRequest.delete.class) SysUserOrgRequest sysUserOrgRequest) {
        sysUserOrgService.del(sysUserOrgRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @PostResource(name = "编辑", path = "/sysUserOrg/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysUserOrgRequest.edit.class) SysUserOrgRequest sysUserOrgRequest) {
        sysUserOrgService.edit(sysUserOrgRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @GetResource(name = "查看详情", path = "/sysUserOrg/detail")
    public ResponseData<SysUserOrg> detail(@Validated(SysUserOrgRequest.detail.class) SysUserOrgRequest sysUserOrgRequest) {
        return new SuccessResponseData<>(sysUserOrgService.detail(sysUserOrgRequest));
    }

    /**
     * 获取列表
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @GetResource(name = "获取列表", path = "/sysUserOrg/list")
    public ResponseData<List<SysUserOrg>> list(SysUserOrgRequest sysUserOrgRequest) {
        return new SuccessResponseData<>(sysUserOrgService.findList(sysUserOrgRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @GetResource(name = "分页查询", path = "/sysUserOrg/page")
    public ResponseData<PageResult<SysUserOrg>> page(SysUserOrgRequest sysUserOrgRequest) {
        return new SuccessResponseData<>(sysUserOrgService.findPage(sysUserOrgRequest));
    }

}
