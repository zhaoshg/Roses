package cn.stylefeng.roses.kernel.sys.api;

import java.util.Set;

/**
 * 用户权限的Api
 *
 * @author fengshuonan
 * @since 2023/7/18 22:51
 */
public interface UserPermissionApi {

    /**
     * 获取当前用户拥有的数据范围类型
     *
     * 枚举值在
     *
     * @author fengshuonan
     * @since 2023/7/18 22:58
     */
    Integer currentUserDataScopeType();

    /**
     * 获取当前用户拥有的所有组织机构id列表
     *
     * @return null或者空数组，代表用户没有访问权限，其他情况则代表用户拥有的组织机构id访问权限
     * @author fengshuonan
     * @since 2023/7/18 22:54
     */
    Set<Long> currentUserOrgScopeList();

}
