package cn.stylefeng.roses.kernel.sys.modular.org.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrganization;
import cn.stylefeng.roses.kernel.sys.modular.org.enums.OrgEventEnums;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 添加组织机构的
 *
 * @author fengshuonan
 * @since 2023/7/14 14:19
 */
@Service
public class AddOrgListener {

    @Resource(name = "sysOrgSubFlagCache")
    private CacheOperatorApi<Boolean> sysOrgSubFlagCache;

    @Override
    public String getBusinessCode() {
        return OrgEventEnums.ADD_ORG_EVENT.name();
    }

    @(OrgEventEnums.ADD_ORG_EVENT)
    public void doCallbackAction(HrOrganization businessObject) {

        if (ObjectUtil.isNotEmpty(businessObject.getOrgId())) {
            sysOrgSubFlagCache.remove(String.valueOf(businessObject.getOrgId()));
        }

        if (ObjectUtil.isNotEmpty(businessObject.getOrgParentId())) {
            sysOrgSubFlagCache.remove(String.valueOf(businessObject.getOrgParentId()));
        }

    }

}
