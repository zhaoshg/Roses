package cn.stylefeng.roses.kernel.sys.modular.role.cache.rolemenu.clear;

import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 监听角色绑定菜单的事件
 *
 * @author fengshuonan
 * @since 2023/7/14 22:48
 */
@Service
public class RoleMenuClearListener {

    @Resource(name = "roleMenuCache")
    private CacheOperatorApi<List<Long>> roleMenuCache;

}
