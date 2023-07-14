package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.event.sdk.publish.BusinessEventPublisher;
import cn.stylefeng.roses.kernel.sys.modular.app.entity.SysApp;
import cn.stylefeng.roses.kernel.sys.modular.app.service.SysAppService;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleAssignOperateAction;
import cn.stylefeng.roses.kernel.sys.modular.role.constants.RoleConstants;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.factory.PermissionAssignFactory;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionItem;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionResponse;
import cn.stylefeng.roses.kernel.sys.modular.role.service.PermissionAssignService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色权限绑定相关的接口
 *
 * @author fengshuonan
 * @since 2023/6/13 16:14
 */
@Service
public class PermissionAssignServiceImpl implements PermissionAssignService {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysAppService sysAppService;

    @Resource
    private SysMenuOptionsService sysMenuOptionsService;

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public RoleBindPermissionResponse getRoleBindPermission(RoleBindPermissionRequest roleBindPermissionRequest) {

        // 1. 整理出一个总的响应的结构树，选择状态为空
        RoleBindPermissionResponse selectTreeStructure = this.createSelectTreeStructure();

        // 2. 获取角色绑定的应用，菜单，功能列表
        Set<Long> roleBindMenusAndOptions = this.getRoleBindMenusAndOptions(roleBindPermissionRequest.getRoleId());

        // 3. 组合结构树和角色绑定的信息，填充选择状态，封装返回结果
        return PermissionAssignFactory.fillCheckedFlag(selectTreeStructure, roleBindMenusAndOptions);
    }

    @Override
    public void updateRoleBindPermission(RoleBindPermissionRequest roleBindPermissionRequest) {
        Map<String, RoleAssignOperateAction> operateActionMap = SpringUtil.getBeansOfType(RoleAssignOperateAction.class);
        for (RoleAssignOperateAction roleAssignOperateAction : operateActionMap.values()) {
            if (roleAssignOperateAction.getNodeType().getCode().equals(roleBindPermissionRequest.getPermissionNodeType())) {
                roleAssignOperateAction.doOperateAction(roleBindPermissionRequest);
                return;
            }
        }

        // 更新角色绑定权限的缓存
        BusinessEventPublisher.publishEvent(RoleConstants.ROLE_BIND_MENU_EVENT, roleBindPermissionRequest.getRoleId());
        BusinessEventPublisher.publishEvent(RoleConstants.ROLE_BIND_MENU_OPTIONS_EVENT, roleBindPermissionRequest.getRoleId());
    }

    @Override
    public RoleBindPermissionResponse createSelectTreeStructure() {

        // 获取所有的菜单
        List<SysMenu> totalMenus = this.sysMenuService.getTotalMenus();

        // 组装所有的叶子节点菜单【初始化菜单】
        List<RoleBindPermissionItem> totalResultMenus = PermissionAssignFactory.createPermissionMenus(totalMenus);

        // 查询菜单对应的所有应用
        Set<Long> appIdList = totalMenus.stream().map(SysMenu::getAppId).collect(Collectors.toSet());
        LambdaQueryWrapper<SysApp> sysAppLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysAppLambdaQueryWrapper.in(SysApp::getAppId, appIdList);
        sysAppLambdaQueryWrapper.select(SysApp::getAppId, SysApp::getAppName);
        sysAppLambdaQueryWrapper.orderByAsc(SysApp::getAppSort);
        List<SysApp> totalAppList = sysAppService.list(sysAppLambdaQueryWrapper);

        // 组装所有的应用节点信息【初始化应用】
        List<RoleBindPermissionItem> totalResultApps = PermissionAssignFactory.createApps(totalAppList);

        // 获取所有的菜单上的功能
        LambdaQueryWrapper<SysMenuOptions> optionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        optionsLambdaQueryWrapper.select(SysMenuOptions::getMenuId, SysMenuOptions::getMenuOptionId, SysMenuOptions::getOptionName);
        Set<String> menuIds = totalResultMenus.stream().map(RoleBindPermissionItem::getNodeId).collect(Collectors.toSet());
        optionsLambdaQueryWrapper.in(SysMenuOptions::getMenuId, menuIds);
        List<SysMenuOptions> sysMenuOptionsList = sysMenuOptionsService.list(optionsLambdaQueryWrapper);

        // 组装所有的应用节点信息【初始化菜单功能】
        List<RoleBindPermissionItem> totalResultOptions = PermissionAssignFactory.createMenuOptions(sysMenuOptionsList);

        // 将应用、菜单、功能组成返回结果
        return PermissionAssignFactory.composeSelectStructure(totalResultApps, totalResultMenus, totalResultOptions);
    }

    @Override
    public Set<Long> getRoleBindMenusAndOptions(Long roleId) {

        HashSet<Long> resultPermissions = new HashSet<>();

        // 获取角色绑定的菜单
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.select(SysRoleMenu::getMenuId);
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> sysRoleMenuList = this.sysRoleMenuService.list(sysRoleMenuLambdaQueryWrapper);
        Set<Long> menuIdSet = sysRoleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet());
        resultPermissions.addAll(menuIdSet);

        // 获取角色绑定的功能
        LambdaQueryWrapper<SysRoleMenuOptions> sysRoleMenuOptionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuOptionsLambdaQueryWrapper.select(SysRoleMenuOptions::getMenuOptionId);
        sysRoleMenuOptionsLambdaQueryWrapper.eq(SysRoleMenuOptions::getRoleId, roleId);
        List<SysRoleMenuOptions> sysRoleMenuOptionsList = this.sysRoleMenuOptionsService.list(sysRoleMenuOptionsLambdaQueryWrapper);
        Set<Long> optionsIds = sysRoleMenuOptionsList.stream().map(SysRoleMenuOptions::getMenuOptionId).collect(Collectors.toSet());
        resultPermissions.addAll(optionsIds);

        return resultPermissions;
    }

}
