package cn.stylefeng.roses.kernel.sys.modular.role.cache.rolemenu.clear;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.event.api.annotation.BusinessListener;
import cn.stylefeng.roses.kernel.sys.modular.role.constants.RoleConstants;
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

    /**
     * 监听角色绑定权限的事件
     *
     * @author fengshuonan
     * @since 2023/7/14 23:06
     */
    @BusinessListener(businessCode = RoleConstants.ROLE_BIND_MENU_EVENT)
    public void roleBindMenuEvent(Long roleId) {
        if (ObjectUtil.isNotEmpty(roleId)) {
            roleMenuCache.remove(roleId.toString());
        }
    }

}
