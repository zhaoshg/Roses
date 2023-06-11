package cn.stylefeng.roses.kernel.sys.modular.position.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.position.entity.HrPosition;
import cn.stylefeng.roses.kernel.sys.modular.position.pojo.request.HrPositionRequest;
import cn.stylefeng.roses.kernel.sys.modular.position.service.HrPositionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 职位信息控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:25
 */
@RestController
@ApiResource(name = "职位信息")
public class HrPositionController {

    @Resource
    private HrPositionService hrPositionService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2023/06/10 21:25
     */
    @PostResource(name = "添加", path = "/hrPosition/add")
    public ResponseData<HrPosition> add(@RequestBody @Validated(HrPositionRequest.add.class) HrPositionRequest hrPositionRequest) {
        hrPositionService.add(hrPositionRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2023/06/10 21:25
     */
    @PostResource(name = "删除", path = "/hrPosition/delete")
    public ResponseData<?> delete(@RequestBody @Validated(HrPositionRequest.delete.class) HrPositionRequest hrPositionRequest) {
        hrPositionService.del(hrPositionRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2023/06/10 21:25
     */
    @PostResource(name = "编辑", path = "/hrPosition/edit")
    public ResponseData<?> edit(@RequestBody @Validated(HrPositionRequest.edit.class) HrPositionRequest hrPositionRequest) {
        hrPositionService.edit(hrPositionRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2023/06/10 21:25
     */
    @GetResource(name = "查看详情", path = "/hrPosition/detail")
    public ResponseData<HrPosition> detail(@Validated(HrPositionRequest.detail.class) HrPositionRequest hrPositionRequest) {
        return new SuccessResponseData<>(hrPositionService.detail(hrPositionRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2023/06/10 21:25
     */
    @GetResource(name = "分页查询", path = "/hrPosition/page")
    public ResponseData<PageResult<HrPosition>> page(HrPositionRequest hrPositionRequest) {
        return new SuccessResponseData<>(hrPositionService.findPage(hrPositionRequest));
    }

}
