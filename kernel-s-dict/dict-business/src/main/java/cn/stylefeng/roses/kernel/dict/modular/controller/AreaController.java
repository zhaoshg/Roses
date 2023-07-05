package cn.stylefeng.roses.kernel.dict.modular.controller;

import cn.stylefeng.roses.kernel.dict.modular.entity.Area;
import cn.stylefeng.roses.kernel.dict.modular.pojo.AreaVo;
import cn.stylefeng.roses.kernel.dict.modular.pojo.request.AreaRequest;
import cn.stylefeng.roses.kernel.dict.modular.service.AreaService;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.enums.ResBizTypeEnum;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 【系统基础】-【行政区域表】控制器
 *
 * @author LiYanJun
 * @date 2023/07/05 18:12
 */
@RestController
@ApiResource(name = "行政区域", resBizType = ResBizTypeEnum.SYSTEM)
public class AreaController {

    @Resource
    private AreaService areaService;

    /**
     * 添加
     *
     * @author LiYanJun
     * @date 2023/07/05 18:12
     */
    @PostResource(name = "添加行政区域", path = "/area/add")
    public ResponseData<Area> add(@RequestBody @Validated(AreaRequest.add.class) AreaRequest areaRequest) {
        areaService.add(areaRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author LiYanJun
     * @date 2023/07/05 18:12
     */
    @PostResource(name = "删除行政区域", path = "/area/delete")
    public ResponseData<?> delete(@RequestBody @Validated(AreaRequest.delete.class) AreaRequest areaRequest) {
        areaService.del(areaRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author LiYanJun
     * @date 2023/07/05 18:12
     */
    @PostResource(name = "编辑行政区域", path = "/area/edit")
    public ResponseData<?> edit(@RequestBody @Validated(AreaRequest.edit.class) AreaRequest areaRequest) {
        areaService.edit(areaRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author LiYanJun
     * @date 2023/07/05 18:12
     */
    @GetResource(name = "查看行政区域详情", path = "/area/detail")
    public ResponseData<Area> detail(@Validated(AreaRequest.detail.class) AreaRequest areaRequest) {
        return new SuccessResponseData<>(areaService.detail(areaRequest));
    }

    /**
     * 获取列表
     *
     * @author LiYanJun
     * @date 2023/07/05 18:12
     */
    @GetResource(name = "获取行政区域列表", path = "/area/list")
    public ResponseData<List<Area>> list(AreaRequest areaRequest) {
        return new SuccessResponseData<>(areaService.findList(areaRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author LiYanJun
     * @date 2023/07/05 18:12
     */
    @GetResource(name = "分页查询行政区域", path = "/area/page")
    public ResponseData<PageResult<AreaVo>> page(AreaRequest areaRequest) {
        return new SuccessResponseData<>(areaService.findPage(areaRequest));
    }

}
