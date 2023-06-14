package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleAssignOperateAction;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.factory.PermissionAssignResultFactory;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionItem;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色绑定权限，点击绑定应用时候的业务处理
 *
 * @author fengshuonan
 * @since 2023/6/14 14:13
 */
public class RoleBindAppImpl implements RoleAssignOperateAction {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysMenuOptionsService sysMenuOptionsService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    @Override
    public PermissionNodeTypeEnum getNodeType() {
        return PermissionNodeTypeEnum.APP;
    }

    @Override
    public List<RoleBindPermissionItem> doOperateAction(RoleBindPermissionRequest roleBindPermissionRequest) {

        Long roleId = roleBindPermissionRequest.getRoleId();
        Long appId = roleBindPermissionRequest.getNodeId();

        // 找到所选应用的对应的所有菜单
        LambdaQueryWrapper<SysMenu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(SysMenu::getAppId, appId);
        menuLambdaQueryWrapper.select(SysMenu::getMenuId, SysMenu::getMenuName, SysMenu::getMenuParentId);
        List<SysMenu> totalMenus = sysMenuService.list(menuLambdaQueryWrapper);
        Set<Long> totalMenuIds = totalMenus.stream().map(SysMenu::getMenuId).collect(Collectors.toSet());

        if (ObjectUtil.isEmpty(totalMenus)) {
            return new ArrayList<>();
        }

        // 找到所选应用的对应的所有菜单功能
        LambdaQueryWrapper<SysMenuOptions> menuOptionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuOptionsLambdaQueryWrapper.eq(SysMenuOptions::getAppId, appId);
        menuOptionsLambdaQueryWrapper.select(SysMenuOptions::getMenuOptionId, SysMenuOptions::getOptionName, SysMenuOptions::getMenuId);
        List<SysMenuOptions> totalMenuOptions = sysMenuOptionsService.list(menuOptionsLambdaQueryWrapper);
        Set<Long> totalMenuOptionIds = totalMenuOptions.stream().map(SysMenuOptions::getMenuOptionId).collect(Collectors.toSet());

        // 先删除角色绑定的这些菜单
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        sysRoleMenuLambdaQueryWrapper.in(SysRoleMenu::getMenuId, totalMenuIds);
        sysRoleMenuService.remove(sysRoleMenuLambdaQueryWrapper);

        // 删除角色绑定的这些菜单功能
        if (ObjectUtil.isNotEmpty(totalMenuOptionIds)) {
            LambdaQueryWrapper<SysRoleMenuOptions> sysRoleMenuOptionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysRoleMenuOptionsLambdaQueryWrapper.eq(SysRoleMenuOptions::getRoleId, roleId);
            sysRoleMenuOptionsLambdaQueryWrapper.in(SysRoleMenuOptions::getMenuOptionId, totalMenuOptionIds);
            sysRoleMenuOptionsService.remove(sysRoleMenuOptionsLambdaQueryWrapper);
        }

        // 如果是选中了应用，则从新绑定这些菜单和功能
        if (roleBindPermissionRequest.getChecked()) {
            List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
            for (SysMenu menuItem : totalMenus) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenu.setAppId(appId);
                sysRoleMenu.setMenuId(menuItem.getMenuId());
                sysRoleMenuList.add(sysRoleMenu);
            }
            this.sysRoleMenuService.saveBatch(sysRoleMenuList);

            List<SysRoleMenuOptions> sysRoleMenuOptionsList = new ArrayList<>();
            for (SysMenuOptions menuOptionItem : totalMenuOptions) {
                SysRoleMenuOptions sysRoleMenuOptions = new SysRoleMenuOptions();
                sysRoleMenuOptions.setRoleId(roleId);
                sysRoleMenuOptions.setAppId(appId);
                sysRoleMenuOptions.setMenuId(menuOptionItem.getMenuId());
                sysRoleMenuOptions.setMenuOptionId(menuOptionItem.getMenuOptionId());
                sysRoleMenuOptionsList.add(sysRoleMenuOptions);
            }
            this.sysRoleMenuOptionsService.saveBatch(sysRoleMenuOptionsList);
        }

        // 组装返回结果
        return PermissionAssignResultFactory.createRoleBindAppResult(totalMenus, totalMenuOptions, roleBindPermissionRequest.getChecked());
    }

}
