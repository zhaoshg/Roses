package cn.stylefeng.roses.kernel.sys.modular.org.cache;

import cn.hutool.cache.impl.TimedCache;
import cn.stylefeng.roses.kernel.cache.memory.AbstractMemoryCacheOperator;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;

/**
 * 组织机构是否包含下级的缓存
 *
 * @author fengshuonan
 * @since 2023/7/14 1:06
 */
public class SysOrgSubFlagMemoryCache extends AbstractMemoryCacheOperator<Boolean> {

    public SysOrgSubFlagMemoryCache(TimedCache<String, Boolean> timedCache) {
        super(timedCache);
    }

    @Override
    public String getCommonKeyPrefix() {
        return SysConstants.SYS_ORG_SUB_FLAG_CACHE_PREFIX;
    }

}