package cn.stylefeng.roses.kernel.sys.api;

import java.util.List;
import java.util.Set;

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

    /**
     * 根据角色id找到角色对应的用户id集合
     *
     * @author fengshuonan
     * @since 2023/5/26 14:08
     */
    List<Long> findUserIdsByRoleId(Long roleId);

    /**
     * 获取用户的角色范围列表，角色范围指用户的角色可分配的权限列表
     *
     * @param userId 用户id
     * @author fengshuonan
     * @since 2023/9/8 21:41
     */
    Set<Long> findUserRoleLimitScope(Long userId);

    /**
     * 获取当前登录用户的角色范围列表，角色范围指用户的角色可分配的权限列表
     *
     * @author fengshuonan
     * @since 2023/9/8 21:41
     */
    Set<Long> findCurrentUserRoleLimitScope();

}
