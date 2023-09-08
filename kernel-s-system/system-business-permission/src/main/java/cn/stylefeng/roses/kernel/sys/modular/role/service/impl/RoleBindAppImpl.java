package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleAssignOperateAction;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleBindLimitAction;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleLimit;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.RoleLimitTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleLimitService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色绑定权限，点击绑定应用时候的业务处理
 *
 * @author fengshuonan
 * @since 2023/6/14 14:13
 */
@Service
@SuppressWarnings("all")
public class RoleBindAppImpl implements RoleAssignOperateAction, RoleBindLimitAction {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysMenuOptionsService sysMenuOptionsService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    @Resource
    private SysRoleLimitService sysRoleLimitService;

    @Override
    public PermissionNodeTypeEnum getNodeType() {
        return PermissionNodeTypeEnum.APP;
    }

    @Override
    public void doOperateAction(RoleBindPermissionRequest roleBindPermissionRequest) {

        Long roleId = roleBindPermissionRequest.getRoleId();
        Long appId = roleBindPermissionRequest.getNodeId();

        // 找到所选应用的对应的所有菜单
        Set<Long> appMenuIds = this.getAppMenuIds(appId);
        if (ObjectUtil.isEmpty(appMenuIds)) {
            return;
        }

        // 找到所选应用的对应的所有菜单功能
        List<SysMenuOptions> totalMenuOptions = this.getAppMenuOptions(appId);
        Set<Long> totalMenuOptionIds = totalMenuOptions.stream().map(SysMenuOptions::getMenuOptionId).collect(Collectors.toSet());

        // 先删除角色绑定的这些菜单
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        sysRoleMenuLambdaQueryWrapper.in(SysRoleMenu::getMenuId, appMenuIds);
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
            for (Long menuId : appMenuIds) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenu.setAppId(appId);
                sysRoleMenu.setMenuId(menuId);
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
    }

    @Override
    public PermissionNodeTypeEnum getRoleBindLimitNodeType() {
        return this.getNodeType();
    }

    @Override
    public void doRoleBindLimitAction(RoleBindPermissionRequest roleBindPermissionRequest) {

        Long roleId = roleBindPermissionRequest.getRoleId();
        Long appId = roleBindPermissionRequest.getNodeId();

        // 找到所选应用的对应的所有菜单
        Set<Long> menuIds = this.getAppMenuIds(appId);

        // 菜单为空，则直接返回
        if (ObjectUtil.isEmpty(menuIds)) {
            return;
        }

        // 找到所选应用的对应的所有菜单功能
        List<SysMenuOptions> totalMenuOptions = this.getAppMenuOptions(appId);
        Set<Long> menuOptionIds = totalMenuOptions.stream().map(SysMenuOptions::getMenuOptionId).collect(Collectors.toSet());

        // 组装菜单id和功能id的集合
        List<Long> totalBusinessId = ListUtil.list(false, menuIds);
        if (ObjectUtil.isNotEmpty(menuOptionIds)) {
            totalBusinessId.addAll(menuOptionIds);
        }

        // 删除角色绑定的这些菜单限制，以及菜单功能限制
        LambdaQueryWrapper<SysRoleLimit> sysRoleLimitLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLimitLambdaQueryWrapper.eq(SysRoleLimit::getRoleId, roleId);
        sysRoleLimitLambdaQueryWrapper.in(SysRoleLimit::getBusinessId, totalBusinessId);
        sysRoleLimitService.remove(sysRoleLimitLambdaQueryWrapper);

        // 如果是选中了应用，则从新绑定这些菜单和功能
        if (roleBindPermissionRequest.getChecked()) {
            List<SysRoleLimit> totalRoleLimit = new ArrayList<>();

            // 绑定菜单id
            for (Long menuId : menuIds) {
                SysRoleLimit sysRoleLimit = new SysRoleLimit();
                sysRoleLimit.setRoleId(roleId);
                sysRoleLimit.setLimitType(RoleLimitTypeEnum.MENU.getCode());
                sysRoleLimit.setBusinessId(menuId);
                totalRoleLimit.add(sysRoleLimit);
            }

            // 绑定菜单功能id
            if (ObjectUtil.isNotEmpty(menuOptionIds)) {
                for (Long optionsId : menuOptionIds) {
                    SysRoleLimit sysRoleLimit = new SysRoleLimit();
                    sysRoleLimit.setRoleId(roleId);
                    sysRoleLimit.setLimitType(RoleLimitTypeEnum.MENU_OPTIONS.getCode());
                    sysRoleLimit.setBusinessId(optionsId);
                    totalRoleLimit.add(sysRoleLimit);
                }
            }

            this.sysRoleLimitService.saveBatch(totalRoleLimit);
        }

    }

    /**
     * 获取应用下的所有菜单id
     *
     * @author fengshuonan
     * @since 2023/9/8 15:03
     */
    private Set<Long> getAppMenuIds(Long appId) {
        LambdaQueryWrapper<SysMenu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(SysMenu::getAppId, appId);
        menuLambdaQueryWrapper.select(SysMenu::getMenuId);
        List<SysMenu> totalMenus = sysMenuService.list(menuLambdaQueryWrapper);
        if (ObjectUtil.isEmpty(totalMenus)) {
            return new HashSet<>();
        }
        return totalMenus.stream().map(SysMenu::getMenuId).collect(Collectors.toSet());
    }

    /**
     * 获取应用下的所有菜单功能
     *
     * @author fengshuonan
     * @since 2023/9/8 15:13
     */
    private List<SysMenuOptions> getAppMenuOptions(Long appId) {
        LambdaQueryWrapper<SysMenuOptions> menuOptionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuOptionsLambdaQueryWrapper.eq(SysMenuOptions::getAppId, appId);
        menuOptionsLambdaQueryWrapper.select(SysMenuOptions::getMenuOptionId, SysMenuOptions::getMenuId);
        return sysMenuOptionsService.list(menuOptionsLambdaQueryWrapper);
    }

}
