package cn.stylefeng.roses.kernel.sys.modular.resource.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.ResourceReportApi;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.ReportResourceParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 接收微服务远程资源
 *
 * @author fengshuonan
 * @since 2023/8/9 0:30
 */
@RestController
@ApiResource(name = "接收微服务远程资源")
public class ResourceReceiveController {

    @Resource
    private ResourceReportApi resourceReportApi;

    /**
     * 接受资源
     *
     * @author fengshuonan
     * @date 2021/8/26 17:28
     */
    @PostResource(name = "接受资源", path = "/resourceService/reportResources")
    public ResponseData<?> reportResources(@RequestBody ReportResourceParam reportResourceReq) {
        resourceReportApi.reportResources(reportResourceReq);
        return new SuccessResponseData<>();
    }

}