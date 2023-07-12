package cn.stylefeng.roses.kernel.sys.api;

import java.util.List;

/**
 * 单独编写用户角色关联关系的Api
 *
 * @author fengshuonan
 * @since 2023/6/18 23:21
 */
public interface SysUserRoleServiceApi {

    /**
     * 获取用户的角色id列表
     *
     * @author fengshuonan
     * @since 2023/6/12 11:29
     */
    List<Long> getUserRoleIdList(Long userId);

}
