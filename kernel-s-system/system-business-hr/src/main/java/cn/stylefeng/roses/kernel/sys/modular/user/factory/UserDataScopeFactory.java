package cn.stylefeng.roses.kernel.sys.modular.user.factory;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.db.api.DbOperatorApi;
import cn.stylefeng.roses.kernel.db.mp.datascope.UserRoleDataScopeApi;
import cn.stylefeng.roses.kernel.db.mp.datascope.config.DataScopeConfig;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUser;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserOrgService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户数据范围条件的拼装
 *
 * @author fengshuonan
 * @since 2024-03-01 16:29
 */
public class UserDataScopeFactory {

    /**
     * 创建用户的带数据范围的条件
     *
     * @author fengshuonan
     * @since 2024-03-01 16:30
     */
    public static void getUserDataScopeCondition(LambdaQueryWrapper<SysUser> queryWrapper) {

        UserRoleDataScopeApi userRoleDataScopeApi = SpringUtil.getBean(UserRoleDataScopeApi.class);
        SysUserOrgService sysUserOrgService = SpringUtil.getBean(SysUserOrgService.class);
        DbOperatorApi dbOperatorApi = SpringUtil.getBean(DbOperatorApi.class);


        // 获取当前用户的数据范围
        DataScopeConfig userRoleDataScopeConfig = userRoleDataScopeApi.getUserRoleDataScopeConfig();

        switch (userRoleDataScopeConfig.getDataScopeType()) {
            // 如果是本人数据
            case SELF:
                queryWrapper.eq(SysUser::getUserId, userRoleDataScopeConfig.getUserId());
                break;

            // 如果是本部门数据
            case DEPT:

                // 获取本部门下的所有用户id
                Set<Long> deptUserIdList = sysUserOrgService.getOrgUserIdList(CollectionUtil.set(false, userRoleDataScopeConfig.getUserDeptId()));
                if (ObjectUtil.isEmpty(deptUserIdList)) {
                    deptUserIdList = CollectionUtil.set(false, -1L);
                }
                queryWrapper.in(SysUser::getUserId, deptUserIdList);
                break;

            // 如果是本部门及有以下部门数据
            case DEPT_WITH_CHILD:

                // 获取本部门及以下部门有哪些部门
                Set<Long> subDeptOrgIdList = dbOperatorApi.findSubListByParentId("sys_hr_organization", "org_pids", "org_id", userRoleDataScopeConfig.getUserDeptId());
                if (ObjectUtil.isEmpty(subDeptOrgIdList)) {
                    subDeptOrgIdList = new HashSet<>();
                }
                subDeptOrgIdList.add(userRoleDataScopeConfig.getUserDeptId());

                // 获取部门下的用户
                Set<Long> subDeptOrgUserIdList = sysUserOrgService.getOrgUserIdList(subDeptOrgIdList);
                if (ObjectUtil.isEmpty(subDeptOrgUserIdList)) {
                    subDeptOrgUserIdList = CollectionUtil.set(false, -1L);
                }

                queryWrapper.in(SysUser::getUserId, subDeptOrgUserIdList);
                break;

            // 如果是本公司及以下部门数据
            case COMPANY_WITH_CHILD:

                // 获取本部门及以下部门有哪些部门
                Set<Long> subCompanyOrgIdList = dbOperatorApi.findSubListByParentId("sys_hr_organization", "org_pids", "org_id", userRoleDataScopeConfig.getUserDeptId());
                if (ObjectUtil.isEmpty(subCompanyOrgIdList)) {
                    subCompanyOrgIdList = new HashSet<>();
                }
                subCompanyOrgIdList.add(userRoleDataScopeConfig.getUserDeptId());

                // 获取部门下的用户
                Set<Long> subCompanyUserIdList = sysUserOrgService.getOrgUserIdList(subCompanyOrgIdList);
                if (ObjectUtil.isEmpty(subCompanyUserIdList)) {
                    subCompanyUserIdList = CollectionUtil.set(false, -1L);
                }

                queryWrapper.in(SysUser::getUserId, subCompanyUserIdList);

                break;

            // 如果是指定部门数据
            case DEFINE:

                // 获取指定部门下的用户列表
                List<Long> specificOrgIds = userRoleDataScopeConfig.getSpecificOrgIds();
                if (ObjectUtil.isEmpty(specificOrgIds)) {
                    specificOrgIds = CollectionUtil.list(false, -1L);
                }

                Set<Long> specificOrgUserIdList = sysUserOrgService.getOrgUserIdList(new HashSet<>(specificOrgIds));
                if (ObjectUtil.isEmpty(specificOrgUserIdList)) {
                    specificOrgUserIdList = CollectionUtil.set(false, -1L);
                }

                queryWrapper.in(SysUser::getUserId, specificOrgUserIdList);
                break;

            // 如果是全部数据
            case ALL:
                break;
        }

    }

}
