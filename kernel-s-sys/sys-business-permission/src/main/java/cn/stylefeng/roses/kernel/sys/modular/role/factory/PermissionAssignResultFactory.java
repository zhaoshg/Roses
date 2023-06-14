package cn.stylefeng.roses.kernel.sys.modular.role.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.tree.factory.DefaultTreeBuildFactory;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限绑定结果的对象创建工厂
 *
 * @author fengshuonan
 * @since 2023/6/13 22:58
 */
public class PermissionAssignResultFactory {

    /**
     * 创建角色绑定菜单的相应结果
     *
     * @author fengshuonan
     * @since 2023/6/13 23:03
     */
    public static List<RoleBindPermissionItem> createRoleBindMenuResult(List<SysMenuOptions> menuOptions,
                                                                        Boolean checkedFlag) {

        List<RoleBindPermissionItem> result = new ArrayList<>();

        if (ObjectUtil.isEmpty(menuOptions)) {
            return result;
        }

        for (SysMenuOptions menuOption : menuOptions) {
            RoleBindPermissionItem roleBindPermissionItem = new RoleBindPermissionItem(menuOption.getMenuOptionId(),
                    null, menuOption.getOptionName(), PermissionNodeTypeEnum.OPTIONS.getCode(), checkedFlag);
            result.add(roleBindPermissionItem);
        }

        return result;
    }

    /**
     * 创建角色绑定应用时候的响应结果
     *
     * @author fengshuonan
     * @since 2023/6/14 14:53
     */
    public static List<RoleBindPermissionItem> createRoleBindAppResult(List<SysMenu> totalMenus,
                                                                       List<SysMenuOptions> totalMenuOptions,
                                                                       Boolean checkedFlag) {

        List<RoleBindPermissionItem> result = new ArrayList<>();

        if (ObjectUtil.isEmpty(totalMenus)) {
            return result;
        }

        for (SysMenu sysMenu : totalMenus) {
            RoleBindPermissionItem roleBindPermissionItem = new RoleBindPermissionItem(sysMenu.getMenuId(), null,
                    sysMenu.getMenuName(), PermissionNodeTypeEnum.MENU.getCode(), checkedFlag);
            result.add(roleBindPermissionItem);
        }

        for (SysMenuOptions sysMenuOptions : totalMenuOptions) {
            RoleBindPermissionItem roleBindPermissionItem = new RoleBindPermissionItem(sysMenuOptions.getMenuOptionId(),
                    sysMenuOptions.getMenuId(), sysMenuOptions.getOptionName(),
                    PermissionNodeTypeEnum.OPTIONS.getCode(), checkedFlag);
            result.add(roleBindPermissionItem);
        }

        return new DefaultTreeBuildFactory<RoleBindPermissionItem>().doTreeBuild(result);
    }

}
