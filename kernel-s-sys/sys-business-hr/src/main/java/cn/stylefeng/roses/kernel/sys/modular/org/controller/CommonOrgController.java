package cn.stylefeng.roses.kernel.sys.modular.org.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrganization;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.HrOrganizationRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.service.HrOrganizationService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通用组织机构接口
 *
 * @author fengshuonan
 * @since 2023/6/11 10:04
 */
@RestController
@ApiResource(name = "通用组织机构接口")
public class CommonOrgController {

    @Resource
    private HrOrganizationService hrOrganizationService;

    /**
     * 通用获取组织机构树
     * <p>
     * ps：用在获取用户管理和组织机构管理界面左侧树
     *
     * @author fengshuonan
     * @since 2023/6/11 10:31
     */
    @GetResource(name = "通用获取组织机构树", path = "/common/org/tree")
    public ResponseData<List<HrOrganization>> commonOrgTree(HrOrganizationRequest hrOrganizationRequest) {
        return new SuccessResponseData<>(hrOrganizationService.commonOrgTree(hrOrganizationRequest));
    }

}
