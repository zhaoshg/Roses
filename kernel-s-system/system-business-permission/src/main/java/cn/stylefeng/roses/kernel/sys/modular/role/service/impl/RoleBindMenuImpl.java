package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleAssignOperateAction;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色绑定菜单的相关操作
 *
 * @author fengshuonan
 * @since 2023/9/8 15:31
 */
@Service
public class RoleBindMenuImpl implements RoleAssignOperateAction {

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysMenuOptionsService sysMenuOptionsService;

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    @Resource
    private SysMenuService sysMenuService;

    @Override
    public PermissionNodeTypeEnum getNodeType() {
        return PermissionNodeTypeEnum.MENU;
    }

    @Override
    public void doOperateAction(RoleBindPermissionRequest roleBindPermissionRequest) {

        Long roleId = roleBindPermissionRequest.getRoleId();
        Long menuId = roleBindPermissionRequest.getNodeId();

        // 1. 先取消绑定角色和菜单
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getMenuId, menuId);
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        this.sysRoleMenuService.remove(sysRoleMenuLambdaQueryWrapper);

        // 2. 如果是选中，则执行菜单和角色的绑定
        // 查询菜单对应的appId，冗余一下appId
        Map<Long, Long> menuAppIdMap = sysMenuService.getMenuAppId(ListUtil.list(false, menuId));
        Long appId = menuAppIdMap.get(menuId);
        if (roleBindPermissionRequest.getChecked()) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setAppId(appId);
            this.sysRoleMenuService.save(sysRoleMenu);
        }

        // 3. 查询菜单下是否有菜单功能，如果有菜单功能，则也直接清空掉
        LambdaQueryWrapper<SysMenuOptions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenuOptions::getMenuId, menuId);
        queryWrapper.select(SysMenuOptions::getMenuOptionId, SysMenuOptions::getOptionName);
        List<SysMenuOptions> totalMenuOptions = sysMenuOptionsService.list(queryWrapper);

        // 菜单下没有菜单功能，则直接返回
        if (ObjectUtil.isEmpty(totalMenuOptions)) {
            return;
        }

        // 4. 如果有菜单功能，则执行先删除后添加的逻辑
        // 先删除角色和菜单功能的绑定
        LambdaQueryWrapper<SysRoleMenuOptions> roleMenuOptionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuOptionsLambdaQueryWrapper.eq(SysRoleMenuOptions::getRoleId, roleId);
        roleMenuOptionsLambdaQueryWrapper.in(SysRoleMenuOptions::getMenuOptionId,
                totalMenuOptions.stream().map(SysMenuOptions::getMenuOptionId).collect(Collectors.toSet()));
        sysRoleMenuOptionsService.remove(roleMenuOptionsLambdaQueryWrapper);

        // 5. 如果是选中状态，则从新进行这些角色和功能的绑定
        if (roleBindPermissionRequest.getChecked()) {
            ArrayList<SysRoleMenuOptions> sysRoleMenuOptions = new ArrayList<>();
            for (SysMenuOptions totalMenuOption : totalMenuOptions) {
                SysRoleMenuOptions roleMenuOptions = new SysRoleMenuOptions();
                roleMenuOptions.setRoleId(roleId);
                roleMenuOptions.setAppId(appId);
                roleMenuOptions.setMenuId(menuId);
                roleMenuOptions.setMenuOptionId(totalMenuOption.getMenuOptionId());
                sysRoleMenuOptions.add(roleMenuOptions);
            }
            this.sysRoleMenuOptionsService.saveBatch(sysRoleMenuOptions);
        }
    }

}
