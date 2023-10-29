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
        stringMaxCountConfigHashMap.put("SYSTEM_BASE_APP", new MaxCountConfig("sys_app", "app_sort"));
        stringMaxCountConfigHashMap.put("SYSTEM_BASE_DICT_TYPE", new MaxCountConfig("sys_dict_type", "dict_type_sort"));
        stringMaxCountConfigHashMap.put("SYSTEM_BASE_DICT", new MaxCountConfig("sys_dict", "dict_sort"));
        stringMaxCountConfigHashMap.put("SYSTEM_HR_ORGANIZATION", new MaxCountConfig("sys_hr_organization", "org_sort"));
        stringMaxCountConfigHashMap.put("SYSTEM_HR_POSITION", new MaxCountConfig("sys_hr_position", "position_sort"));
        stringMaxCountConfigHashMap.put("SYSTEM_BASE_MENU", new MaxCountConfig("sys_menu", "menu_sort"));
        stringMaxCountConfigHashMap.put("SYSTEM_BASE_ROLE", new MaxCountConfig("sys_role", "role_sort"));
        stringMaxCountConfigHashMap.put("SYSTEM_HR_USER", new MaxCountConfig("sys_user", "user_sort"));
        return stringMaxCountConfigHashMap;
    }

}
