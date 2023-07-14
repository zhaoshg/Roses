package cn.stylefeng.roses.kernel.sys.modular.menu.cache;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.event.api.annotation.BusinessListener;
import cn.stylefeng.roses.kernel.sys.modular.menu.constants.MenuConstants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 监听菜单的清空
 *
 * @author fengshuonan
 * @since 2023/7/15 0:08
 */
@Service
public class MenuClearListener {

    @Resource(name = "menuCodeCache")
    private CacheOperatorApi<String> menuCodeCache;

    /**
     * 监听菜单的更新，清除缓存
     *
     * @author fengshuonan
     * @since 2023/7/15 0:21
     */
    @BusinessListener(businessCode = MenuConstants.MENU_UPDATE_EVENT)
    public void updateMenuCodeCache(Long menuId) {
        if (ObjectUtil.isNotEmpty(menuId)) {
            menuCodeCache.remove(menuId.toString());
        }
    }

}
