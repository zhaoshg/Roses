package cn.stylefeng.roses.kernel.sys.api.maxsort;

import cn.stylefeng.roses.kernel.db.api.maxsort.MaxCountConfig;
import cn.stylefeng.roses.kernel.db.api.maxsort.MaxSortCollectorApi;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * system模块的最大排序数获取的配置
 *
 * @author fengshuonan
 * @since 2023/10/29 17:51
 */
@Service
public class SystemMaxSortFieldConfig implements MaxSortCollectorApi {

    @Override
    public Map<String, MaxCountConfig> createMaxSortConfigs() {
        HashMap<String, MaxCountConfig> stringMaxCountConfigHashMap = new HashMap<>();
        stringMaxCountConfigHashMap.put("user", new MaxCountConfig("sys_user", "user_sort"));
        return stringMaxCountConfigHashMap;
    }

}
