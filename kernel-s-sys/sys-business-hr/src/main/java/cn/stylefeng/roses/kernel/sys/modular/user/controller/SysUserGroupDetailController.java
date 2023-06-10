package cn.stylefeng.roses.kernel.sys.modular.user.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserGroupDetail;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserGroupDetailRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserGroupDetailService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户组详情控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@RestController
@ApiResource(name = "用户组详情")
public class SysUserGroupDetailController {

    @Resource
    private SysUserGroupDetailService sysUserGroupDetailService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @PostResource(name = "添加", path = "/sysUserGroupDetail/add")
    public ResponseData<SysUserGroupDetail> add(@RequestBody @Validated(SysUserGroupDetailRequest.add.class) SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        sysUserGroupDetailService.add(sysUserGroupDetailRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @PostResource(name = "删除", path = "/sysUserGroupDetail/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysUserGroupDetailRequest.delete.class) SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        sysUserGroupDetailService.del(sysUserGroupDetailRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @PostResource(name = "编辑", path = "/sysUserGroupDetail/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysUserGroupDetailRequest.edit.class) SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        sysUserGroupDetailService.edit(sysUserGroupDetailRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @GetResource(name = "查看详情", path = "/sysUserGroupDetail/detail")
    public ResponseData<SysUserGroupDetail> detail(@Validated(SysUserGroupDetailRequest.detail.class) SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        return new SuccessResponseData<>(sysUserGroupDetailService.detail(sysUserGroupDetailRequest));
    }

    /**
     * 获取列表
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @GetResource(name = "获取列表", path = "/sysUserGroupDetail/list")
    public ResponseData<List<SysUserGroupDetail>> list(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        return new SuccessResponseData<>(sysUserGroupDetailService.findList(sysUserGroupDetailRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    @GetResource(name = "分页查询", path = "/sysUserGroupDetail/page")
    public ResponseData<PageResult<SysUserGroupDetail>> page(SysUserGroupDetailRequest sysUserGroupDetailRequest) {
        return new SuccessResponseData<>(sysUserGroupDetailService.findPage(sysUserGroupDetailRequest));
    }

}
