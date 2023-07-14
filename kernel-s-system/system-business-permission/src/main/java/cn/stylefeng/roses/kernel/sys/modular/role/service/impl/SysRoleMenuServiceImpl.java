package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.event.sdk.publish.BusinessEventPublisher;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveMenuCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleAssignOperateAction;
import cn.stylefeng.roses.kernel.sys.modular.role.constants.RoleConstants;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleMenuMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色菜单关联业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService,
        RemoveRoleCallbackApi, RoleAssignOperateAction, RemoveMenuCallbackApi {

    @Resource
    private SysMenuOptionsService sysMenuOptionsService;

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource(name = "roleMenuCache")
    private CacheOperatorApi<List<Long>> roleMenuCache;

    @Override
    public void bindRoleMenus(Long roleId, List<SysMenu> menuList) {

        if (ObjectUtil.isEmpty(roleId) || ObjectUtil.isEmpty(menuList)) {
            return;
        }

        // 清空角色的相关角色菜单关联
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        this.remove(queryWrapper);

        // 绑定角色菜单
        ArrayList<SysRoleMenu> sysRoleMenus = new ArrayList<>();
        for (SysMenu sysMenu : menuList) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setAppId(sysMenu.getAppId());
            sysRoleMenu.setMenuId(sysMenu.getMenuId());
            sysRoleMenus.add(sysRoleMenu);
        }
        this.getBaseMapper().insertBatchSomeColumn(sysRoleMenus);
    }

    @Override
    public List<Long> getRoleBindMenuIdList(List<Long> roleIdList) {

        List<Long> result = new ArrayList<>();

        if (ObjectUtil.isEmpty(roleIdList)) {
            return result;
        }

        for (Long roleId : roleIdList) {

            String roleIdKey = String.valueOf(roleId);

            // 先从缓存中获取，是否有绑定的菜单
            List<Long> cacheMenuIdList = roleMenuCache.get(roleIdKey);

            if (ObjectUtil.isNotEmpty(cacheMenuIdList)) {
                result.addAll(cacheMenuIdList);
                continue;
            }

            // 缓存中没有，则从数据库中查询
            LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
            sysRoleMenuLambdaQueryWrapper.select(SysRoleMenu::getMenuId);
            List<SysRoleMenu> sysRoleMenuList = this.list(sysRoleMenuLambdaQueryWrapper);
            if (ObjectUtil.isNotEmpty(sysRoleMenuList)) {

                List<Long> menuIdListQueryResult = sysRoleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
                result.addAll(menuIdListQueryResult);

                // 将查询结果加入到缓存中
                roleMenuCache.put(roleIdKey, menuIdListQueryResult, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
            }
        }

        return result;
    }

    @Override
    public boolean validateRoleHaveAppIdPermission(List<Long> roleIdList, Long appId) {
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.in(SysRoleMenu::getRoleId, roleIdList);
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getAppId, appId);
        return this.count(sysRoleMenuLambdaQueryWrapper) > 0;
    }

    @Override
    public void validateHaveRoleBind(Set<Long> beRemovedRoleIdList) {
        // none
    }

    @Override
    public void removeRoleAction(Set<Long> beRemovedRoleIdList) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysRoleMenu::getRoleId, beRemovedRoleIdList);
        this.remove(wrapper);
    }

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
        this.remove(sysRoleMenuLambdaQueryWrapper);

        // 2. 如果是选中，则执行菜单和角色的绑定
        // 查询菜单对应的appId，冗余一下appId
        Map<Long, Long> menuAppIdMap = sysMenuService.getMenuAppId(ListUtil.list(false, menuId));
        Long appId = menuAppIdMap.get(menuId);
        if (roleBindPermissionRequest.getChecked()) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setAppId(appId);
            this.save(sysRoleMenu);
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

        // 6. 发布角色更新绑定菜单的事件
        BusinessEventPublisher.publishEvent(RoleConstants.ROLE_BIND_MENU_EVENT, roleId);
    }

    @Override
    public void removeMenuAction(Set<Long> beRemovedMenuIdList) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleMenu::getMenuId, beRemovedMenuIdList);
        this.remove(queryWrapper);
    }

}