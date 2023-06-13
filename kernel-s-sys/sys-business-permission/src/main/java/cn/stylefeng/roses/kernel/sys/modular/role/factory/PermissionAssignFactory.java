package cn.stylefeng.roses.kernel.sys.modular.role.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.rule.tree.factory.DefaultTreeBuildFactory;
import cn.stylefeng.roses.kernel.sys.modular.app.entity.SysApp;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionItem;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限分配相关的实体创建
 *
 * @author fengshuonan
 * @since 2023/6/13 16:30
 */
public class PermissionAssignFactory {

    /**
     * 创建权限绑定的菜单列表
     * <p>
     * 注意： 菜单必须是最子节点，也就是叶子节点
     *
     * @author fengshuonan
     * @since 2023/6/13 16:32
     */
    public static List<RoleBindPermissionItem> createPermissionMenus(List<SysMenu> sysMenus) {

        if (ObjectUtil.isEmpty(sysMenus)) {
            return new ArrayList<>();
        }

        // 搜集所有的父级菜单id
        Set<Long> totalParentMenuId = sysMenus.stream().map(SysMenu::getMenuParentId).collect(Collectors.toSet());

        // 通过父级菜单，筛选出来所有的叶子节点（如果菜单不存在父级菜单里，则代表是叶子节点）
        Set<SysMenu> leafMenus = sysMenus.stream().filter(item -> !totalParentMenuId.contains(item)).collect(Collectors.toSet());

        // 叶子节点转化为RoleBindPermissionItem结构
        ArrayList<RoleBindPermissionItem> roleBindPermissionItems = new ArrayList<>();

        for (SysMenu leafMenu : leafMenus) {
            RoleBindPermissionItem roleBindPermissionItem = new RoleBindPermissionItem(leafMenu.getMenuId(), leafMenu.getAppId(), leafMenu.getMenuName(), PermissionNodeTypeEnum.MENU.getCode(), false);
            roleBindPermissionItems.add(roleBindPermissionItem);
        }

        return roleBindPermissionItems;
    }

    /**
     * 创建权限绑定的应用信息
     *
     * @author fengshuonan
     * @since 2023/6/13 17:00
     */
    public static List<RoleBindPermissionItem> createApps(List<SysApp> sysApps) {

        if (ObjectUtil.isEmpty(sysApps)) {
            return new ArrayList<>();
        }

        ArrayList<RoleBindPermissionItem> appResults = new ArrayList<>();

        // 封装响应结果
        for (SysApp sysApp : sysApps) {
            RoleBindPermissionItem roleBindPermissionItem = new RoleBindPermissionItem(sysApp.getAppId(), TreeConstants.DEFAULT_PARENT_ID, sysApp.getAppName(), PermissionNodeTypeEnum.APP.getCode(), false);
            appResults.add(roleBindPermissionItem);
        }

        return appResults;
    }

    /**
     * 创建菜单功能信息
     *
     * @author fengshuonan
     * @since 2023/6/13 17:25
     */
    public static List<RoleBindPermissionItem> createMenuOptions(List<SysMenuOptions> sysMenuOptionsList) {

        if (ObjectUtil.isEmpty(sysMenuOptionsList)) {
            return new ArrayList<>();
        }

        ArrayList<RoleBindPermissionItem> optionsResult = new ArrayList<>();

        // 封装响应结果
        for (SysMenuOptions sysMenuOptions : sysMenuOptionsList) {
            RoleBindPermissionItem roleBindPermissionItem = new RoleBindPermissionItem(sysMenuOptions.getMenuOptionId(), sysMenuOptions.getMenuId(), sysMenuOptions.getOptionName(), PermissionNodeTypeEnum.OPTIONS.getCode(), false);
            optionsResult.add(roleBindPermissionItem);
        }

        return optionsResult;
    }

    /**
     * 组合成角色绑定权限需要的详情信息，一颗树形结构，选中状态都是未选中
     *
     * @param apps    应用信息
     * @param menus   菜单信息
     * @param options 功能信息
     * @author fengshuonan
     * @since 2023/6/13 17:43
     */
    public static RoleBindPermissionResponse composeSelectStructure(List<RoleBindPermissionItem> apps, List<RoleBindPermissionItem> menus, List<RoleBindPermissionItem> options) {

        // 定义全选属性
        RoleBindPermissionResponse roleBindPermissionResponse = new RoleBindPermissionResponse();
        roleBindPermissionResponse.setChecked(false);

        // 合并应用菜单和功能，并构建树形结构
        apps.addAll(menus);
        apps.addAll(options);

        List<RoleBindPermissionItem> roleBindPermissionItems = new DefaultTreeBuildFactory<RoleBindPermissionItem>().doTreeBuild(apps);
        roleBindPermissionResponse.setAppPermissionList(roleBindPermissionItems);

        return roleBindPermissionResponse;
    }

    /**
     * 将空状态的权限树，填充角色绑定的权限
     *
     * @param roleBindPermissionResponse 空状态的角色权限树（未设置选中状态）
     * @param rolePermissions            角色所拥有的菜单id和功能id的集合
     * @author fengshuonan
     * @since 2023/6/13 19:00
     */
    public static RoleBindPermissionResponse fillCheckedFlag(RoleBindPermissionResponse roleBindPermissionResponse, Set<Long> rolePermissions) {

        List<RoleBindPermissionItem> appList = roleBindPermissionResponse.getAppPermissionList();

        // 开始填充菜单和功能的选中状态
        fillSubItemCheckedFlag(appList, rolePermissions);

        // 填充应用的选中状态
        for (RoleBindPermissionItem appItem : appList) {
            fillParentCheckedFlag(appItem);
        }

        // 填充全选的选中状态
        roleBindPermissionResponse.setChecked(true);
        for (RoleBindPermissionItem appItem : appList) {
            if (!appItem.getChecked()) {
                roleBindPermissionResponse.setChecked(false);
            }
        }

        return roleBindPermissionResponse;
    }

    /**
     * 填充子节点的选中状态
     * <p>
     * 根据执行的角色权限参数匹配判断
     *
     * @author fengshuonan
     * @since 2023/6/13 19:21
     */
    private static void fillSubItemCheckedFlag(List<RoleBindPermissionItem> beFilled, Set<Long> rolePermissionList) {

        if (ObjectUtil.isEmpty(beFilled) || ObjectUtil.isEmpty(rolePermissionList)) {
            return;
        }

        for (RoleBindPermissionItem roleBindPermissionItem : beFilled) {
            if (rolePermissionList.contains(roleBindPermissionItem.getNodeId())) {
                roleBindPermissionItem.setChecked(true);
            }

            fillSubItemCheckedFlag(roleBindPermissionItem.getChildren(), rolePermissionList);
        }
    }

    /**
     * 填充父级的节点的选中状态
     * <p>
     * 如果所有子集都选中了，则选中所有的父级状态
     *
     * @author fengshuonan
     * @since 2023/6/13 19:25
     */
    private static void fillParentCheckedFlag(RoleBindPermissionItem beFilled) {

        if (ObjectUtil.isEmpty(beFilled)) {
            return;
        }

        beFilled.setChecked(true);
        for (RoleBindPermissionItem item : beFilled.getChildren()) {
            if (!item.getChecked()) {
                beFilled.setChecked(false);
            }
        }
    }

}
