package cn.stylefeng.roses.kernel.sys.modular.org.controller;

import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.sys.modular.org.service.HrOrgApproverService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 组织机构审批人控制器
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
@RestController
@ApiResource(name = "组织机构审批人")
public class HrOrgApproverController {

    @Resource
    private HrOrgApproverService hrOrgApproverService;

    /**
     * 获取组织机构审批人类型列表
     *
     * @author fengshuonan
     * @since 2022/9/26 10:44
     */
    @GetResource(name = "获取组织机构审批人类型列表", path = "/hrOrgApprover/getApproverTypeList")
    public ResponseData<List<SimpleDict>> getApproverTypeList() {
        return new SuccessResponseData<>(hrOrgApproverService.getApproverTypeList());
    }

}
