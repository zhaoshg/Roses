package cn.stylefeng.roses.kernel.sys.api;

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

}
