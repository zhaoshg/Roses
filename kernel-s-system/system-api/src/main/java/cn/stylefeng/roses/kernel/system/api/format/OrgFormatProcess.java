package cn.stylefeng.roses.kernel.system.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.format.BaseSimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.system.api.OrganizationServiceApi;
import cn.stylefeng.roses.kernel.system.api.pojo.organization.HrOrganizationDTO;

/**
 * JSON响应对组织机构id的转化
 *
 * @author fengshuonan
 * @since 2023/5/4 21:20
 */
public class OrgFormatProcess extends BaseSimpleFieldFormatProcess {

    @Override
    public Class<?> getItemClass() {
        return Long.class;
    }

    @Override
    public Object simpleItemFormat(Object businessId) {
        Long orgId = Convert.toLong(businessId);
        OrganizationServiceApi bean = SpringUtil.getBean(OrganizationServiceApi.class);
        HrOrganizationDTO orgDetail = bean.getOrgDetail(orgId);
        if (orgDetail == null) {
            return null;
        }
        return orgDetail.getOrgName();
    }

}
