package cn.stylefeng.roses.kernel.sys.modular.tablewidth.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.entity.SysTableWidth;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.pojo.request.SysTableWidthRequest;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.service.SysTableWidthService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 业务中表的宽度控制器
 *
 * @author fengshuonan
 * @date 2023/02/23 22:21
 */
@RestController
@ApiResource(name = "业务中表的宽度")
public class SysTableWidthController {

    @Resource
    private SysTableWidthService sysTableWidthService;

    /**
     * 获取用户针对某个业务的table的列宽配置
     *
     * @author fengshuonan
     * @date 2023/02/23 22:21
     */
    @GetResource(name = "获取用户针对某个业务的table的列宽配置", path = "/sysTableWidth/getUserConfig")
    public ResponseData<SysTableWidth> getUserConfig(
            @Validated(SysTableWidthRequest.detail.class) SysTableWidthRequest sysTableWidthRequest) {
        return new SuccessResponseData<>(sysTableWidthService.detail(sysTableWidthRequest));
    }

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2023/02/23 22:21
     */
    @PostResource(name = "添加", path = "/sysTableWidth/setTableWidth")
    public ResponseData<SysTableWidth> setTableWidth(
            @RequestBody @Validated(SysTableWidthRequest.add.class) SysTableWidthRequest sysTableWidthRequest) {
        sysTableWidthService.setTableWidth(sysTableWidthRequest);
        return new SuccessResponseData<>();
    }

}
