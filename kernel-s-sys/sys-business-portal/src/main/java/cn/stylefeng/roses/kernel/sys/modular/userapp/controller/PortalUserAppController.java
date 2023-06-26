package cn.stylefeng.roses.kernel.sys.modular.userapp.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.userapp.entity.PortalUserApp;
import cn.stylefeng.roses.kernel.sys.modular.userapp.pojo.request.PortalUserAppRequest;
import cn.stylefeng.roses.kernel.sys.modular.userapp.service.PortalUserAppService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户常用功能控制器
 *
 * @author fengshuonan
 * @date 2023/06/26 21:25
 */
@RestController
@ApiResource(name = "用户常用功能")
public class PortalUserAppController {

    @Resource
    private PortalUserAppService portalUserAppService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    @PostResource(name = "添加", path = "/portalUserApp/add")
    public ResponseData<PortalUserApp> add(@RequestBody @Validated(PortalUserAppRequest.add.class) PortalUserAppRequest portalUserAppRequest) {
        portalUserAppService.add(portalUserAppRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    @PostResource(name = "删除", path = "/portalUserApp/delete")
    public ResponseData<?> delete(@RequestBody @Validated(PortalUserAppRequest.delete.class) PortalUserAppRequest portalUserAppRequest) {
        portalUserAppService.del(portalUserAppRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    @PostResource(name = "编辑", path = "/portalUserApp/edit")
    public ResponseData<?> edit(@RequestBody @Validated(PortalUserAppRequest.edit.class) PortalUserAppRequest portalUserAppRequest) {
        portalUserAppService.edit(portalUserAppRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    @GetResource(name = "查看详情", path = "/portalUserApp/detail")
    public ResponseData<PortalUserApp> detail(@Validated(PortalUserAppRequest.detail.class) PortalUserAppRequest portalUserAppRequest) {
        return new SuccessResponseData<>(portalUserAppService.detail(portalUserAppRequest));
    }

    /**
     * 获取列表
     *
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    @GetResource(name = "获取列表", path = "/portalUserApp/list")
    public ResponseData<List<PortalUserApp>> list(PortalUserAppRequest portalUserAppRequest) {
        return new SuccessResponseData<>(portalUserAppService.findList(portalUserAppRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    @GetResource(name = "分页查询", path = "/portalUserApp/page")
    public ResponseData<PageResult<PortalUserApp>> page(PortalUserAppRequest portalUserAppRequest) {
        return new SuccessResponseData<>(portalUserAppService.findPage(portalUserAppRequest));
    }

}
