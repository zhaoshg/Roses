package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.db.api.DbOperatorApi;
import cn.stylefeng.roses.kernel.db.mp.datascope.UserRoleDataScopeApi;
import cn.stylefeng.roses.kernel.db.mp.datascope.config.DataScopeConfig;
import cn.stylefeng.roses.kernel.rule.enums.permission.DataScopeTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.SysUserRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRole;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleDataScopeService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户的数据范围的获取实现
 *
 * @author fengshuonan
 * @since 2024-03-01 14:06
 */
@Service
public class UserRoleDataScopeImpl implements UserRoleDataScopeApi {

    @Resource
    private SysUserRoleServiceApi sysUserRoleServiceApi;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysRoleDataScopeService sysRoleDataScopeService;

    @Resource
    private DbOperatorApi dbOperatorApi;

    @Override
    public DataScopeConfig getUserRoleDataScopeConfig() {

        // 获取当前登录用户id
        LoginUser loginUser = LoginContext.me().getLoginUserNullable();
        if (loginUser == null) {
            return null;
        }

        // 获取用户的角色id集合
        List<Long> userRoleIdList = sysUserRoleServiceApi.getUserRoleIdList(loginUser.getUserId());

        // 如果这个角色列表大于1，也就是不单纯是普通角色，则将普通角色去掉，以配置的角色为主
        Long defaultRoleId = sysRoleService.getDefaultRoleId();
        if (CollectionUtil.contains(userRoleIdList, defaultRoleId)) {
            userRoleIdList.remove(defaultRoleId);
        }

        // 获取这些角色对应的【最高】的数据范围，取数据范围10-50最大的数字
        Integer maxDataScope = 0;
        Long finalRoleId = null;
        List<SysRole> roleDataScopeType = sysRoleService.getRoleDataScopeType(userRoleIdList);
        for (SysRole sysRole : roleDataScopeType) {
            if (sysRole.getDataScopeType() > maxDataScope) {
                maxDataScope = sysRole.getDataScopeType();
                finalRoleId = sysRole.getRoleId();
            }
        }

        DataScopeConfig dataScopeConfig = new DataScopeConfig();

        // 设置数据最终的数据范围
        dataScopeConfig.setDataScopeType(DataScopeTypeEnum.codeToEnum(maxDataScope));

        // 如果数据范围是指定部门，则需要单独查下这个角色对应的部门数据有哪些
        if (DataScopeTypeEnum.DEFINE.getCode().equals(maxDataScope)) {
            Set<Long> roleBindOrgIdList = sysRoleDataScopeService.getRoleBindOrgIdList(ListUtil.list(false, finalRoleId));
            dataScopeConfig.setSpecificOrgIds(new ArrayList<>(roleBindOrgIdList));
        }

        // 设置用户id
        dataScopeConfig.setUserId(loginUser.getUserId());

        // 如果数据范围是本公司及以下，则查询当前用户的公司id
        if (DataScopeTypeEnum.COMPANY_WITH_CHILD.getCode().equals(maxDataScope)) {
            Long currentUserCompanyId = LoginContext.me().getCurrentUserCompanyId();
            dataScopeConfig.setUserCompanyId(currentUserCompanyId);
        }

        // 如果是本部门数据，或者本部门及以下，需要查询当前用户的部门id
        if (DataScopeTypeEnum.DEPT.getCode().equals(maxDataScope) || DataScopeTypeEnum.DEPT_WITH_CHILD.getCode().equals(maxDataScope)) {
            dataScopeConfig.setUserDeptId(loginUser.getCurrentOrgId());
        }

        return dataScopeConfig;
    }

    @Override
    public Set<Long> currentUserOrgScopeList() {

        // 获取当前用户id
        Long userId = LoginContext.me().getLoginUser().getUserId();

        // 获取当前用户的数据范围类型
        DataScopeConfig userRoleDataScopeConfig = this.getUserRoleDataScopeConfig();
        DataScopeTypeEnum dataScopeTypeEnum = userRoleDataScopeConfig.getDataScopeType();

        // 如果是只有本人数据
        if (DataScopeTypeEnum.SELF.equals(dataScopeTypeEnum)) {
            return CollectionUtil.set(false, userId);
        }

        // 如果是本部门数据
        else if (DataScopeTypeEnum.DEPT.equals(dataScopeTypeEnum)) {
            return CollectionUtil.set(false, userRoleDataScopeConfig.getUserDeptId());
        }

        // 如果是本部门及以下部门
        else if (DataScopeTypeEnum.DEPT_WITH_CHILD.equals(dataScopeTypeEnum)) {

            // 获取指定组织机构下的所有机构id
            Set<Long> subOrgIdList = dbOperatorApi.findSubListByParentId("sys_hr_organization", "org_pids", "org_id", userRoleDataScopeConfig.getUserDeptId());
            if (ObjectUtil.isEmpty(subOrgIdList)) {
                subOrgIdList = new HashSet<>();
            }
            subOrgIdList.add(userRoleDataScopeConfig.getUserDeptId());
            return subOrgIdList;
        }

        // 如果是本公司及以下部门
        else if (DataScopeTypeEnum.COMPANY_WITH_CHILD.equals(dataScopeTypeEnum)) {

            // 获取指定组织机构下的所有机构id
            Set<Long> subOrgIdList = dbOperatorApi.findSubListByParentId("sys_hr_organization", "org_pids", "org_id", userRoleDataScopeConfig.getUserCompanyId());
            if (ObjectUtil.isEmpty(subOrgIdList)) {
                subOrgIdList = new HashSet<>();
            }
            subOrgIdList.add(userRoleDataScopeConfig.getUserCompanyId());
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
