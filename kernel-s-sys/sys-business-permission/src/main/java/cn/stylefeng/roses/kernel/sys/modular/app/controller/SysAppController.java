package cn.stylefeng.roses.kernel.sys.modular.app.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.app.entity.SysApp;
import cn.stylefeng.roses.kernel.sys.modular.app.pojo.request.SysAppRequest;
import cn.stylefeng.roses.kernel.sys.modular.app.service.SysAppService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统应用控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@RestController
@ApiResource(name = "系统应用")
public class SysAppController {

    @Resource
    private SysAppService sysAppService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "添加", path = "/sysApp/add")
    public ResponseData<SysApp> add(@RequestBody @Validated(SysAppRequest.add.class) SysAppRequest sysAppRequest) {
        sysAppService.add(sysAppRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "删除", path = "/sysApp/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysAppRequest.delete.class) SysAppRequest sysAppRequest) {
        sysAppService.del(sysAppRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @PostResource(name = "编辑", path = "/sysApp/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysAppRequest.edit.class) SysAppRequest sysAppRequest) {
        sysAppService.edit(sysAppRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @GetResource(name = "查看详情", path = "/sysApp/detail")
    public ResponseData<SysApp> detail(@Validated(SysAppRequest.detail.class) SysAppRequest sysAppRequest) {
        return new SuccessResponseData<>(sysAppService.detail(sysAppRequest));
    }

    /**
     * 获取列表
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @GetResource(name = "获取列表", path = "/sysApp/list")
    public ResponseData<List<SysApp>> list(SysAppRequest sysAppRequest) {
        return new SuccessResponseData<>(sysAppService.findList(sysAppRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    @GetResource(name = "分页查询", path = "/sysApp/page")
    public ResponseData<PageResult<SysApp>> page(SysAppRequest sysAppRequest) {
        return new SuccessResponseData<>(sysAppService.findPage(sysAppRequest));
    }

}
