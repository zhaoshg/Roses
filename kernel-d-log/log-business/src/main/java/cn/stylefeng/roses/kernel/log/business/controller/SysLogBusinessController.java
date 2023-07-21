package cn.stylefeng.roses.kernel.log.business.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.business.entity.SysLogBusiness;
import cn.stylefeng.roses.kernel.log.business.pojo.request.SysLogBusinessRequest;
import cn.stylefeng.roses.kernel.log.business.service.SysLogBusinessService;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业务日志记录控制器
 *
 * @author fengshuonan
 * @date 2023/07/21 19:02
 */
@RestController
@ApiResource(name = "业务日志记录")
public class SysLogBusinessController {

    @Resource
    private SysLogBusinessService sysLogBusinessService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    @PostResource(name = "添加", path = "/sysLogBusiness/add")
    public ResponseData<SysLogBusiness> add(@RequestBody @Validated(SysLogBusinessRequest.add.class) SysLogBusinessRequest sysLogBusinessRequest) {
        sysLogBusinessService.add(sysLogBusinessRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    @PostResource(name = "删除", path = "/sysLogBusiness/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysLogBusinessRequest.delete.class) SysLogBusinessRequest sysLogBusinessRequest) {
        sysLogBusinessService.del(sysLogBusinessRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    @PostResource(name = "编辑", path = "/sysLogBusiness/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysLogBusinessRequest.edit.class) SysLogBusinessRequest sysLogBusinessRequest) {
        sysLogBusinessService.edit(sysLogBusinessRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    @GetResource(name = "查看详情", path = "/sysLogBusiness/detail")
    public ResponseData<SysLogBusiness> detail(@Validated(SysLogBusinessRequest.detail.class) SysLogBusinessRequest sysLogBusinessRequest) {
        return new SuccessResponseData<>(sysLogBusinessService.detail(sysLogBusinessRequest));
    }

    /**
     * 获取列表
     *
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    @GetResource(name = "获取列表", path = "/sysLogBusiness/list")
    public ResponseData<List<SysLogBusiness>> list(SysLogBusinessRequest sysLogBusinessRequest) {
        return new SuccessResponseData<>(sysLogBusinessService.findList(sysLogBusinessRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    @GetResource(name = "分页查询", path = "/sysLogBusiness/page")
    public ResponseData<PageResult<SysLogBusiness>> page(SysLogBusinessRequest sysLogBusinessRequest) {
        return new SuccessResponseData<>(sysLogBusinessService.findPage(sysLogBusinessRequest));
    }

}
