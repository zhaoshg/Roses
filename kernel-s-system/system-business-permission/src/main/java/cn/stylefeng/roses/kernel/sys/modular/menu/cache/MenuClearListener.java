package cn.stylefeng.roses.kernel.sys.modular.menu.cache;

import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
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


}
