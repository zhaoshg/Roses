package cn.stylefeng.roses.kernel.db.mp.datascope;

import cn.stylefeng.roses.kernel.db.mp.datascope.config.DataScopeConfig;

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

}
