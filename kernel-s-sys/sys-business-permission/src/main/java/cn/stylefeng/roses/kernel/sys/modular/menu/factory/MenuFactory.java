package cn.stylefeng.roses.kernel.sys.modular.menu.factory;

import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 菜单信息组装工厂
 *
 * @author fengshuonan
 * @since 2023/6/14 22:05
 */
public class MenuFactory {

    /**
     * 获取指定参数的菜单集合，缺失的所有的父级菜单id
     * <p>
     * 此方法用在菜单管理界面，带搜索条件查询时，组装菜单树使用
     *
     * @author fengshuonan
     * @since 2023/6/14 22:05
     */
    public static Set<Long> getMenuParentIds(List<SysMenu> sysMenus) {

        // 已经有的菜单id集合
        Set<Long> alreadyMenuIdList = sysMenus.stream().map(SysMenu::getMenuId).collect(Collectors.toSet());

        // 需要补全的父级id集合
        Set<Long> needToAddResult = new HashSet<>();

        // 遍历菜单，找到所有的菜单父级集合
        for (SysMenu sysMenu : sysMenus) {
            String menuPidsStr = sysMenu.getMenuPids();

            // 去掉中括号符号
            menuPidsStr = menuPidsStr.replaceAll("\\[", "");
            menuPidsStr = menuPidsStr.replaceAll("]", "");

            // 获取所有上级id列表
            String[] menuParentIdList = menuPidsStr.split(",");

            // 查找到缺失的父级id
            for (String parentMenuIdItem : menuParentIdList) {
                Long menuId = Long.valueOf(parentMenuIdItem);
                if (!alreadyMenuIdList.contains(menuId)) {
                    if (!parentMenuIdItem.equals(TreeConstants.DEFAULT_PARENT_ID.toString())) {
                        needToAddResult.add(menuId);
                    }
                }
            }
        }

        return needToAddResult;
    }

}
