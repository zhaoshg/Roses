package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveMenuCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleAssignOperateAction;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleMenuOptionsMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色和菜单下的功能关联业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Service
public class SysRoleMenuOptionsServiceImpl extends ServiceImpl<SysRoleMenuOptionsMapper, SysRoleMenuOptions> implements
        SysRoleMenuOptionsService, RemoveRoleCallbackApi, RoleAssignOperateAction, RemoveMenuCallbackApi {

    @Override
    public void removeRoleBindOptions(Long optionsId) {
        LambdaQueryWrapper<SysRoleMenuOptions> sysRoleMenuOptionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuOptionsLambdaQueryWrapper.eq(SysRoleMenuOptions::getMenuOptionId, optionsId);
        this.remove(sysRoleMenuOptionsLambdaQueryWrapper);
    }

    @Override
    public void bindRoleMenuOptions(Long roleId, List<SysMenuOptions> sysMenuOptionsList) {

        if (ObjectUtil.isEmpty(roleId) || ObjectUtil.isEmpty(sysMenuOptionsList)) {
            return;
        }

        // 清空角色绑定的菜单功能
        LambdaQueryWrapper<SysRoleMenuOptions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenuOptions::getRoleId, roleId);
        this.remove(queryWrapper);

        // 绑定角色的菜单功能
        ArrayList<SysRoleMenuOptions> sysRoleMenuOptionList = new ArrayList<>();
        for (SysMenuOptions sysMenuOptions : sysMenuOptionsList) {
            SysRoleMenuOptions roleMenuOptionItem = new SysRoleMenuOptions();
            roleMenuOptionItem.setRoleId(roleId);
            roleMenuOptionItem.setMenuOptionId(sysMenuOptions.getMenuOptionId());
            roleMenuOptionItem.setMenuId(sysMenuOptions.getMenuId());
            roleMenuOptionItem.setAppId(sysMenuOptions.getAppId());
            sysRoleMenuOptionList.add(roleMenuOptionItem);
        }
        this.getBaseMapper().insertBatchSomeColumn(sysRoleMenuOptionList);
    }

    @Override
    public List<Long> getRoleBindMenuOptionsIdList(List<Long> roleIdList) {

        if (ObjectUtil.isEmpty(roleIdList)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysRoleMenuOptions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleMenuOptions::getRoleId, roleIdList);
        queryWrapper.select(SysRoleMenuOptions::getMenuOptionId);
        List<SysRoleMenuOptions> roleMenuOptions = this.list(queryWrapper);

        if (ObjectUtil.isEmpty(roleMenuOptions)) {
            return new ArrayList<>();
        }

        return roleMenuOptions.stream().map(SysRoleMenuOptions::getMenuOptionId).collect(Collectors.toList());
    }

    @Override
    public void validateHaveRoleBind(Set<Long> beRemovedRoleIdList) {
        // none
    }

    @Override
    public void removeRoleAction(Set<Long> beRemovedRoleIdList) {
        LambdaQueryWrapper<SysRoleMenuOptions> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysRoleMenuOptions::getRoleId, beRemovedRoleIdList);
        this.remove(wrapper);
    }

    @Override
    public PermissionNodeTypeEnum getNodeType() {
        return PermissionNodeTypeEnum.OPTIONS;
    }

    @Override
    public void doOperateAction(RoleBindPermissionRequest roleBindPermissionRequest) {

        Long roleId = roleBindPermissionRequest.getRoleId();
        Long menuOptionId = roleBindPermissionRequest.getNodeId();

        if (roleBindPermissionRequest.getChecked()) {
            SysRoleMenuOptions sysRoleMenuOptions = new SysRoleMenuOptions();
            sysRoleMenuOptions.setRoleId(roleId);
            sysRoleMenuOptions.setMenuOptionId(menuOptionId);
            this.save(sysRoleMenuOptions);
        } else {
            LambdaUpdateWrapper<SysRoleMenuOptions> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(SysRoleMenuOptions::getRoleId, roleId);
            wrapper.eq(SysRoleMenuOptions::getMenuOptionId, menuOptionId);
            this.remove(wrapper);
        }
    }

    @Override
    public void removeMenuAction(Set<Long> beRemovedMenuIdList) {
        LambdaQueryWrapper<SysRoleMenuOptions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleMenuOptions::getMenuId, beRemovedMenuIdList);
        this.remove(queryWrapper);
    }

}