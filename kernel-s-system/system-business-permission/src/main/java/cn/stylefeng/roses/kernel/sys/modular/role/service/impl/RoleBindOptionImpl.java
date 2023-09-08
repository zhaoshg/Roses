package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleAssignOperateAction;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleBindLimitAction;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleLimit;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.RoleLimitTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleLimitService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 角色绑定功能的限制
 *
 * @author fengshuonan
 * @since 2023/9/8 16:12
 */
@Service
public class RoleBindOptionImpl implements RoleAssignOperateAction, RoleBindLimitAction {

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    @Resource
    private SysRoleLimitService sysRoleLimitService;

    @Override
    public PermissionNodeTypeEnum getNodeType() {
        return PermissionNodeTypeEnum.OPTIONS;
    }

    @Override
    public void doOperateAction(RoleBindPermissionRequest roleBindPermissionRequest, Set<Long> roleLimitMenuIdsAndOptionIds) {

        Long roleId = roleBindPermissionRequest.getRoleId();
        Long menuOptionId = roleBindPermissionRequest.getNodeId();

        // 非法操作
        if (ObjectUtil.isNotEmpty(roleLimitMenuIdsAndOptionIds) && !roleLimitMenuIdsAndOptionIds.contains(menuOptionId)) {
            return;
        }

        if (roleBindPermissionRequest.getChecked()) {
            SysRoleMenuOptions sysRoleMenuOptions = new SysRoleMenuOptions();
            sysRoleMenuOptions.setRoleId(roleId);
            sysRoleMenuOptions.setMenuOptionId(menuOptionId);
            this.sysRoleMenuOptionsService.save(sysRoleMenuOptions);
        } else {
            LambdaUpdateWrapper<SysRoleMenuOptions> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(SysRoleMenuOptions::getRoleId, roleId);
            wrapper.eq(SysRoleMenuOptions::getMenuOptionId, menuOptionId);
            this.sysRoleMenuOptionsService.remove(wrapper);
        }
    }

    @Override
    public PermissionNodeTypeEnum getRoleBindLimitNodeType() {
        return this.getNodeType();
    }

    @Override
    public void doRoleBindLimitAction(RoleBindPermissionRequest roleBindPermissionRequest) {

        Long roleId = roleBindPermissionRequest.getRoleId();
        Long menuOptionId = roleBindPermissionRequest.getNodeId();

        if (roleBindPermissionRequest.getChecked()) {
            SysRoleLimit sysRoleLimit = new SysRoleLimit();
            sysRoleLimit.setRoleId(roleId);
            sysRoleLimit.setLimitType(RoleLimitTypeEnum.MENU_OPTIONS.getCode());
            sysRoleLimit.setBusinessId(menuOptionId);
            this.sysRoleLimitService.save(sysRoleLimit);
        } else {
            LambdaUpdateWrapper<SysRoleLimit> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(SysRoleLimit::getRoleId, roleId);
            wrapper.eq(SysRoleLimit::getBusinessId, menuOptionId);
            this.sysRoleLimitService.remove(wrapper);
        }

    }

}
