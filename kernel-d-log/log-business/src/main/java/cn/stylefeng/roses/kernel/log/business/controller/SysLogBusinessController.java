package cn.stylefeng.roses.kernel.log.business.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.business.entity.SysLogBusiness;
import cn.stylefeng.roses.kernel.log.business.pojo.request.SysLogBusinessRequest;
import cn.stylefeng.roses.kernel.log.business.service.SysLogBusinessService;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 业务日志记录控制器
 *
 * @author fengshuonan
 * @date 2023/07/21 15:00
 */
@RestController
@ApiResource(name = "业务日志记录")
public class SysLogBusinessController {

    @Resource
    private SysLogBusinessService sysLogBusinessService;

    /**
     * 查询业务日志列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/07/21 15:00
     */
    @GetResource(name = "查询业务日志列表（带分页）", path = "/sysLogBusiness/page")
    public ResponseData<PageResult<SysLogBusiness>> page(SysLogBusinessRequest sysLogBusinessRequest) {
        return new SuccessResponseData<>(sysLogBusinessService.findPage(sysLogBusinessRequest));
    }

}
