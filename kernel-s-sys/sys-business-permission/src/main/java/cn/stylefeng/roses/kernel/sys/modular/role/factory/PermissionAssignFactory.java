package cn.stylefeng.roses.kernel.sys.modular.role.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.sys.modular.app.entity.SysApp;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionItem;

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
            RoleBindPermissionItem roleBindPermissionItem = new RoleBindPermissionItem();

            // 设置节点id和名称为菜单id和菜单名称
            roleBindPermissionItem.setNodeId(leafMenu.getMenuId());
            roleBindPermissionItem.setNodeName(leafMenu.getMenuName());

            // 设置节点类型为2-菜单
            roleBindPermissionItem.setPermissionNodeType(PermissionNodeTypeEnum.MENU.getCode());

            // 默认未选中
            roleBindPermissionItem.setChecked(false);

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

            RoleBindPermissionItem roleBindPermissionItem = new RoleBindPermissionItem();

            // 设置应用id
            roleBindPermissionItem.setNodeId(sysApp.getAppId());

            // 设置应用名称
            roleBindPermissionItem.setNodeName(sysApp.getAppName());

            // 设置未选中
            roleBindPermissionItem.setChecked(false);

            // 设置类型为1-应用
            roleBindPermissionItem.setPermissionNodeType(PermissionNodeTypeEnum.APP.getCode());

            appResults.add(roleBindPermissionItem);
        }

        return appResults;
    }

}
