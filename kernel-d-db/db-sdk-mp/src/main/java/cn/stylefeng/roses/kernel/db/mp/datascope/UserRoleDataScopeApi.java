package cn.stylefeng.roses.kernel.db.mp.datascope;

import cn.stylefeng.roses.kernel.db.mp.datascope.config.DataScopeConfig;

import java.util.Set;

/**
 * 获取用户角色的数据范围
 *
 * @author fengshuonan
 * @since 2024-03-01 13:39
 */
public interface UserRoleDataScopeApi {

    /**
     * 获取当前用户的数据范围
     *
     * @author fengshuonan
     * @since 2024-03-01 13:41
     */
    DataScopeConfig getUserRoleDataScopeConfig();

    /**
     * 获取当前用户拥有的所有组织机构id列表
     * <p>
     * 返回值说明：
     * 1. 可以返回null或者空，代表用户有所有权限，也就是全部数据
     * 2. 返回带有userId或者orgId的选项，代表用户有这些人或者这些机构的权限
     * 3. 返回带有负数（例如：-1）的数组，则代表用户没有任何权限
     *
     * @return 用户拥有的数据范围，userId或者orgId
     * @author fengshuonan
     * @since 2023/7/18 22:54
     */
    Set<Long> currentUserOrgScopeList();

}
