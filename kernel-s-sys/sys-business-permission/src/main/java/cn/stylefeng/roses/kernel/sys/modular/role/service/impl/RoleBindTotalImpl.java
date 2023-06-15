package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleAssignOperateAction;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionItem;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色绑定权限，点击绑定所有时候的业务处理
 *
 * @author fengshuonan
 * @since 2023/6/14 14:13
 */
public class RoleBindTotalImpl implements RoleAssignOperateAction {

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
        return PermissionNodeTypeEnum.TOTAL;
    }

    @Override
    public List<RoleBindPermissionItem> doOperateAction(RoleBindPermissionRequest roleBindPermissionRequest) {

        Long roleId = roleBindPermissionRequest.getRoleId();

        // 清空用户绑定的所有菜单和功能
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        this.sysRoleMenuService.remove(sysRoleMenuLambdaQueryWrapper);

        LambdaQueryWrapper<SysRoleMenuOptions> sysRoleMenuOptionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuOptionsLambdaQueryWrapper.eq(SysRoleMenuOptions::getRoleId, roleId);
        this.sysRoleMenuOptionsService.remove(sysRoleMenuOptionsLambdaQueryWrapper);

        // 如果是选中状态，则从新绑定所有的选项
        if (roleBindPermissionRequest.getChecked()) {

            // 获取所有的菜单
            List<SysMenu> totalMenus = this.sysMenuService.getTotalMenus();

            // 绑定角色的所有菜单
            List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
            for (SysMenu menuItem : totalMenus) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenu.setAppId(menuItem.getAppId());
                sysRoleMenu.setMenuId(menuItem.getMenuId());
                sysRoleMenuList.add(sysRoleMenu);
            }
            this.sysRoleMenuService.saveBatch(sysRoleMenuList);

            // 获取所有的功能
            LambdaQueryWrapper<SysMenuOptions> optionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
            optionsLambdaQueryWrapper.select(SysMenuOptions::getAppId, SysMenuOptions::getMenuId, SysMenuOptions::getMenuOptionId,
                    SysMenuOptions::getOptionName);
            List<SysMenuOptions> sysMenuOptionsList = sysMenuOptionsService.list(optionsLambdaQueryWrapper);

            // 绑定角色的所有功能
            List<SysRoleMenuOptions> sysRoleMenuOptionsList = new ArrayList<>();
            for (SysMenuOptions menuOptionItem : sysMenuOptionsList) {
                SysRoleMenuOptions sysRoleMenuOptions = new SysRoleMenuOptions();
                sysRoleMenuOptions.setRoleId(roleId);
                sysRoleMenuOptions.setAppId(menuOptionItem.getAppId());
                sysRoleMenuOptions.setMenuId(menuOptionItem.getMenuId());
                sysRoleMenuOptions.setMenuOptionId(menuOptionItem.getMenuOptionId());
                sysRoleMenuOptionsList.add(sysRoleMenuOptions);
            }
            this.sysRoleMenuOptionsService.saveBatch(sysRoleMenuOptionsList);
        }

        // 返回空数组，前端处理渲染逻辑
        return new ArrayList<>();
    }

}
