package cn.stylefeng.roses.kernel.sys.modular.org.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.event.api.annotation.BusinessListener;
import cn.stylefeng.roses.kernel.sys.modular.org.constants.OrgConstants;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrganization;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 添加组织机构的事件监听器
 *
 * @author fengshuonan
 * @since 2023/7/14 14:19
 */
@Service
public class OrgOperateListener {

    @Resource(name = "sysOrgSubFlagCache")
    private CacheOperatorApi<Boolean> sysOrgSubFlagCache;

    /**
     * 监听新增组织机构，删除相关的缓存
     *
     * @author fengshuonan
     * @since 2023/7/14 16:22
     */
    @BusinessListener(businessCode = OrgConstants.ADD_ORG_EVENT)
    public void addOrgCallback(HrOrganization businessObject) {

        if (ObjectUtil.isNotEmpty(businessObject.getOrgId())) {
            sysOrgSubFlagCache.remove(String.valueOf(businessObject.getOrgId()));
        }

        if (ObjectUtil.isNotEmpty(businessObject.getOrgParentId())) {
            sysOrgSubFlagCache.remove(String.valueOf(businessObject.getOrgParentId()));
        }

    }

}
