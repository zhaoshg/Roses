package cn.stylefeng.roses.kernel.sys.api;

import cn.stylefeng.roses.kernel.sys.api.enums.org.DetectModeEnum;

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

}
