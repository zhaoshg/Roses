package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveMenuCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleAssignOperateAction;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.exception.SysRoleMenuExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.factory.PermissionAssignResultFactory;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleMenuMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleMenuRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionItem;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public void add(SysRoleMenuRequest sysRoleMenuRequest) {
        SysRoleMenu sysRoleMenu = new SysRoleMenu();
        BeanUtil.copyProperties(sysRoleMenuRequest, sysRoleMenu);
        this.save(sysRoleMenu);
    }

    @Override
    public void del(SysRoleMenuRequest sysRoleMenuRequest) {
        SysRoleMenu sysRoleMenu = this.querySysRoleMenu(sysRoleMenuRequest);
        this.removeById(sysRoleMenu.getRoleMenuId());
    }

    @Override
    public void edit(SysRoleMenuRequest sysRoleMenuRequest) {
        SysRoleMenu sysRoleMenu = this.querySysRoleMenu(sysRoleMenuRequest);
        BeanUtil.copyProperties(sysRoleMenuRequest, sysRoleMenu);
        this.updateById(sysRoleMenu);
    }

    @Override
    public SysRoleMenu detail(SysRoleMenuRequest sysRoleMenuRequest) {
        return this.querySysRoleMenu(sysRoleMenuRequest);
    }

    @Override
    public PageResult<SysRoleMenu> findPage(SysRoleMenuRequest sysRoleMenuRequest) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = createWrapper(sysRoleMenuRequest);
        Page<SysRoleMenu> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

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

        if (ObjectUtil.isEmpty(roleIdList)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.in(SysRoleMenu::getRoleId, roleIdList);
        sysRoleMenuLambdaQueryWrapper.select(SysRoleMenu::getMenuId);
        List<SysRoleMenu> sysRoleMenuList = this.list(sysRoleMenuLambdaQueryWrapper);
        if (ObjectUtil.isEmpty(sysRoleMenuList)) {
            return new ArrayList<>();
        }

        return sysRoleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
    }

    @Override
    public boolean validateRoleHaveAppIdPermission(List<Long> roleIdList, Long appId) {
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.in(SysRoleMenu::getRoleId, roleIdList);
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getAppId, appId);
        return this.count(sysRoleMenuLambdaQueryWrapper) > 0;
    }

    @Override
    public List<SysRoleMenu> findList(SysRoleMenuRequest sysRoleMenuRequest) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = this.createWrapper(sysRoleMenuRequest);
        return this.list(wrapper);
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
    public List<RoleBindPermissionItem> doOperateAction(RoleBindPermissionRequest roleBindPermissionRequest) {

        Long roleId = roleBindPermissionRequest.getRoleId();
        Long menuId = roleBindPermissionRequest.getNodeId();

        // 1. 首先进行角色和菜单的绑定
        SysRoleMenu sysRoleMenu = new SysRoleMenu();
        sysRoleMenu.setRoleId(roleId);
        sysRoleMenu.setMenuId(menuId);
        Long appId = sysMenuService.getMenuAppId(menuId);
        sysRoleMenu.setAppId(appId);

        // 2. 查询菜单下所有的菜单功能
        LambdaQueryWrapper<SysMenuOptions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenuOptions::getMenuId, menuId);
        queryWrapper.select(SysMenuOptions::getMenuOptionId, SysMenuOptions::getOptionName);
        List<SysMenuOptions> totalMenuOptions = sysMenuOptionsService.list(queryWrapper);

        // 3. 先删除已经绑定的所有角色和功能的绑定
        LambdaQueryWrapper<SysRoleMenuOptions> roleMenuOptionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuOptionsLambdaQueryWrapper.eq(SysRoleMenuOptions::getRoleId, roleId);
        roleMenuOptionsLambdaQueryWrapper.in(SysRoleMenuOptions::getMenuOptionId,
                totalMenuOptions.stream().map(SysMenuOptions::getMenuOptionId).collect(Collectors.toSet()));
        sysRoleMenuOptionsService.remove(roleMenuOptionsLambdaQueryWrapper);

        // 4. 如果是选中状态，则从新进行这些角色和功能的绑定
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

        // 5. 根据菜单下的资源信息，封装返回结果
        return PermissionAssignResultFactory.createRoleBindMenuResult(totalMenuOptions, roleBindPermissionRequest.getChecked());
    }

    @Override
    public void removeMenuAction(Set<Long> beRemovedMenuIdList) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleMenu::getMenuId, beRemovedMenuIdList);
        this.remove(queryWrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private SysRoleMenu querySysRoleMenu(SysRoleMenuRequest sysRoleMenuRequest) {
        SysRoleMenu sysRoleMenu = this.getById(sysRoleMenuRequest.getRoleMenuId());
        if (ObjectUtil.isEmpty(sysRoleMenu)) {
            throw new ServiceException(SysRoleMenuExceptionEnum.SYS_ROLE_MENU_NOT_EXISTED);
        }
        return sysRoleMenu;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private LambdaQueryWrapper<SysRoleMenu> createWrapper(SysRoleMenuRequest sysRoleMenuRequest) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();

        Long roleId = sysRoleMenuRequest.getRoleId();
        queryWrapper.eq(ObjectUtil.isNotNull(roleId), SysRoleMenu::getRoleId, roleId);

        Long menuId = sysRoleMenuRequest.getMenuId();
        queryWrapper.eq(ObjectUtil.isNotNull(menuId), SysRoleMenu::getMenuId, menuId);

        return queryWrapper;
    }

}