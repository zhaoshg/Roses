package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleBindLimitAction;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleLimit;
import cn.stylefeng.roses.kernel.sys.modular.role.factory.PermissionAssignFactory;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleLimitMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionResponse;
import cn.stylefeng.roses.kernel.sys.modular.role.service.PermissionAssignService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleLimitService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色权限限制业务实现层
 *
 * @author fengshuonan
 * @date 2023/09/08 12:55
 */
@Service
public class SysRoleLimitServiceImpl extends ServiceImpl<SysRoleLimitMapper, SysRoleLimit> implements SysRoleLimitService {

    @Resource
    private PermissionAssignService permissionAssignService;

    @Override
    public RoleBindPermissionResponse getRoleLimit(RoleBindPermissionRequest roleBindPermissionRequest) {

        // 1. 整理出来一个总的相应的结构树，选择状态为空
        RoleBindPermissionResponse selectTreeStructure = permissionAssignService.createSelectTreeStructure();

        // 2. 获取角色限制所对应的菜单和功能列表
        Set<Long> roleBindLimitList = this.getRoleBindLimitList(roleBindPermissionRequest.getRoleId());

        // 3. 组合结构和角色绑定的限制信息，填充选择状态，封装返回结果
        return PermissionAssignFactory.fillCheckedFlag(selectTreeStructure, roleBindLimitList);
    }

    @Override
    public void updateRoleBindLimit(RoleBindPermissionRequest roleBindPermissionRequest) {
        Map<String, RoleBindLimitAction> operateActionMap = SpringUtil.getBeansOfType(RoleBindLimitAction.class);
        for (RoleBindLimitAction roleBindLimitAction : operateActionMap.values()) {
            if (roleBindLimitAction.getRoleBindLimitNodeType().getCode().equals(roleBindPermissionRequest.getPermissionNodeType())) {
                roleBindLimitAction.doRoleBindLimitAction(roleBindPermissionRequest);
                return;
            }
        }
    }

    /**
     * 获取角色绑定的限制列表（包含限制的菜单id和限制的菜单功能id）
     *
     * @author fengshuonan
     * @since 2023/9/8 13:55
     */
    @Override
    public Set<Long> getRoleBindLimitList(Long roleId) {
        LambdaQueryWrapper<SysRoleLimit> sysRoleLimitLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLimitLambdaQueryWrapper.select(SysRoleLimit::getBusinessId);
        sysRoleLimitLambdaQueryWrapper.eq(SysRoleLimit::getRoleId, roleId);
        List<SysRoleLimit> sysRoleMenuList = this.list(sysRoleLimitLambdaQueryWrapper);
        if (ObjectUtil.isEmpty(sysRoleMenuList)) {
            return new HashSet<>();
        }
        return sysRoleMenuList.stream().map(SysRoleLimit::getBusinessId).collect(Collectors.toSet());
    }

}