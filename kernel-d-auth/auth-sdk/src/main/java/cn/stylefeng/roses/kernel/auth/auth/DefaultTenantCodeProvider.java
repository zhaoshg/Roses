package cn.stylefeng.roses.kernel.auth.auth;

import cn.stylefeng.roses.kernel.auth.api.TenantCodeGetApi;
import cn.stylefeng.roses.kernel.sys.api.expander.TenantConfigExpander;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取默认的租户id
 * <p>
 * 开源版不提供租户管理的功能，只能提供单租户编码的维护，也就是默认租户
 *
 * @author fengshuonan
 * @since 2023/9/1 0:38
 */
@Slf4j
public class DefaultTenantCodeProvider implements TenantCodeGetApi {

    /**
     * 一个不存在的租户id
     */
    public static final Long DEFAULT_NONE_TENANT_ID = -999L;

    @Override
    public Long getTenantIdByCode(String tenantCode) {
        Long defaultRootTenantId = TenantConfigExpander.getDefaultRootTenantId();
        if (defaultRootTenantId.toString().equals(tenantCode)) {
            return defaultRootTenantId;
        } else {
            log.warn("当前系统为单租户系统，不支持多租户功能！");
            return DEFAULT_NONE_TENANT_ID;
        }
    }

}
