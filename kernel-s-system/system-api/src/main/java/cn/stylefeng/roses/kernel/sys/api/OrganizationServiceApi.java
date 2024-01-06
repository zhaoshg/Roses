package cn.stylefeng.roses.kernel.sys.api;

import cn.stylefeng.roses.kernel.sys.api.enums.org.DetectModeEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.CompanyDeptDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.HrOrganizationDTO;

/**
 * 组织机构信息的api
 *
 * @author fengshuonan
 * @since 2023/7/14 16:47
 */
public interface OrganizationServiceApi {

    /**
     * 获取组织机构的名称，通过组织机构id
     *
     * @author fengshuonan
     * @since 2023/7/14 16:47
     */
    String getOrgNameById(Long orgId);

    /**
     * 获取指定组织机构的上级组织机构是什么
     * <p>
     * 自下而上：逐级向上获取直到获取到最高级
     * 自上而下：逐级向下获取，直到获取到本层机构
     *
     * @param orgId          指定机构id
     * @param parentLevelNum 上级机构的层级数，从0开始，0代表直接返回本部门
     * @param detectModeEnum 自上而下还是自下而上
     * @return 上级机构的id
     * @author fengshuonan
     * @since 2022/9/18 15:02
     */
    Long getParentLevelOrgId(Long orgId, Integer parentLevelNum, DetectModeEnum detectModeEnum);

    /**
     * 根据组织机构id，获取对应的具体的公司和部门信息
     *
     * @param orgId 组织机构id
     * @return 公司和部门信息
     * @author fengshuonan
     * @since 2023/6/12 15:42
     */
    CompanyDeptDTO getCompanyDeptInfo(Long orgId);

    /**
     * 根据组织机构id，获取这个组织机构id对应的公司部门信息
     *
     * @param orgId 组织机构id
     * @return 单独返回公司信息
     * @author fengshuonan
     * @since 2023/7/2 8:38
     */
    CompanyDeptDTO getOrgCompanyInfo(Long orgId);

    /**
     * 通过组织机构id获取组织机构信息
     *
     * @author fengshuonan
     * @since 2024/1/6 11:21
     */
    HrOrganizationDTO getOrgInfo(Long orgId);

}
