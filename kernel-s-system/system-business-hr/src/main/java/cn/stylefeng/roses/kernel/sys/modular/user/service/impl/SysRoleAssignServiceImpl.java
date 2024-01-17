package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.stylefeng.roses.kernel.sys.api.SysRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.SysRoleDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserOrgDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.NewUserRoleBindResponse;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.UserRoleDTO;
import cn.stylefeng.roses.kernel.sys.modular.user.factory.RoleAssignFactory;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysRoleAssignService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserOrgService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 新的用户授权界面业务
 *
 * @author fengshuonan
 * @since 2024/1/17 23:08
 */
@Service
public class SysRoleAssignServiceImpl implements SysRoleAssignService {

    @Resource
    private SysUserOrgService sysUserOrgService;

    @Resource
    private SysRoleServiceApi sysRoleServiceApi;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Override
    public List<NewUserRoleBindResponse> getUserAssignList(Long userId) {

        // 1. 获取用户的所有绑定的组织机构信息
        List<UserOrgDTO> userOrgList = sysUserOrgService.getUserOrgList(userId, true);

        // 2. 组装NewUserRoleBindResponse
        List<NewUserRoleBindResponse> baseResponse = RoleAssignFactory.createBaseResponse(userOrgList);

        // 3. 获取系统中，所有的业务角色（type=15），以及所有的公司角色（type=20）
        List<SysRoleDTO> businessRoleAndCompanyRole = sysRoleServiceApi.getBusinessRoleAndCompanyRole(
                userOrgList.stream().map(UserOrgDTO::getCompanyId).collect(Collectors.toList()));

        // 4. 组装每个机构下的角色信息下拉列表
        RoleAssignFactory.fillRoleSelectList(baseResponse, businessRoleAndCompanyRole);

        // 5. 获取用户已经绑定的关联机构的角色（为了赋值选中状态）
        List<UserRoleDTO> userLinkedOrgRoleList = sysUserRoleService.getUserLinkedOrgRoleList(userId);

        // 6. 填充绑定关系，返回结果
        return RoleAssignFactory.fillRoleBind(baseResponse, userLinkedOrgRoleList);
    }

}