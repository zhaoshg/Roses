package cn.stylefeng.roses.kernel.sys.api;

import cn.stylefeng.roses.kernel.sys.api.enums.permission.DataScopeTypeEnum;

import java.util.Set;

/**
 * 用户数据范围权限的Api
 *
 * @author fengshuonan
 * @since 2023/7/18 22:51
 */
public interface UserDataScopeApi {

    /**
     * 获取当前用户拥有的数据范围类型
     *
     * @author fengshuonan
     * @since 2023/7/18 22:58
     */
    DataScopeTypeEnum currentUserDataScopeType();

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
