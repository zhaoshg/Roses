package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.db.api.DbOperatorApi;
import cn.stylefeng.roses.kernel.event.sdk.publish.BusinessEventPublisher;
import cn.stylefeng.roses.kernel.sys.api.SysUserRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.api.enums.permission.DataScopeTypeEnum;
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
import cn.stylefeng.roses.kernel.sys.modular.role.service.*;
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

    @Resource
    private SysUserRoleServiceApi sysUserRoleServiceApi;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private DbOperatorApi dbOperatorApi;

    @Resource
    private SysRoleDataScopeService sysRoleDataScopeService;

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

                // 更新角色绑定权限的缓存
                BusinessEventPublisher.publishEvent(RoleConstants.ROLE_BIND_MENU_EVENT, roleBindPermissionRequest.getRoleId());
                BusinessEventPublisher.publishEvent(RoleConstants.ROLE_BIND_MENU_OPTIONS_EVENT, roleBindPermissionRequest.getRoleId());

                return;
            }
        }
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

    @Override
    public DataScopeTypeEnum currentUserDataScopeType() {

        // 获取当前用户id
        Long userId = LoginContext.me().getLoginUser().getUserId();

        // 获取用户的角色列表
        List<Long> userHaveRoleIds = sysUserRoleServiceApi.getUserRoleIdList(userId);

        // 获取这些角色对应的数据范围
        return sysRoleService.getRoleDataScope(userHaveRoleIds);
    }

    @Override
    public Set<Long> currentUserOrgScopeList() {

        // 获取当前用户id
        Long userId = LoginContext.me().getLoginUser().getUserId();

        // 用户当前组织机构id
        Long currentOrgId = LoginContext.me().getLoginUser().getCurrentOrgId();

        // 获取当前用户的数据范围类型
        DataScopeTypeEnum dataScopeTypeEnum = this.currentUserDataScopeType();

        // 如果是只有本人数据
        if (DataScopeTypeEnum.SELF.equals(dataScopeTypeEnum)) {
            return CollectionUtil.set(false, userId);
        }

        // 如果是本部门数据
        else if (DataScopeTypeEnum.DEPT.equals(dataScopeTypeEnum)) {
            return CollectionUtil.set(false, currentOrgId);
        }

        // 如果是本部门及以下部门
        else if (DataScopeTypeEnum.DEPT_WITH_CHILD.equals(dataScopeTypeEnum)) {

            // 获取指定组织机构下的所有机构id
            Set<Long> subOrgIdList = dbOperatorApi.findSubListByParentId("sys_hr_organization", "org_pids", "org_id", currentOrgId);
            if (ObjectUtil.isEmpty(subOrgIdList)) {
                subOrgIdList = new HashSet<>();
            }
            subOrgIdList.add(currentOrgId);
            return subOrgIdList;
        }

        // 如果是指定部门数据
        else if (DataScopeTypeEnum.DEFINE.equals(dataScopeTypeEnum)) {

            // 获取用户的角色列表
            List<Long> userHaveRoleIds = sysUserRoleServiceApi.getUserRoleIdList(userId);

            // 获取角色指定的所有部门范围
            return sysRoleDataScopeService.getRoleBindOrgIdList(userHaveRoleIds);
        }

        // 如果是全部数据
        else if (DataScopeTypeEnum.ALL.equals(dataScopeTypeEnum)) {

            return null;
        }

        // 默认返回只有本人数据
        return CollectionUtil.set(false, userId);
    }

}
