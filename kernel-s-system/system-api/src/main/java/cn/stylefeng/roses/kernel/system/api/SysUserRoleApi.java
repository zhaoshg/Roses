package cn.stylefeng.roses.kernel.system.api;

import java.util.List;

/**
 * @Author: yx
 * @Date: 2022-12-29 13:27
 */
public interface SysUserRoleApi {
    /**
     * 根据userId查询角色集合
     *
     * @param userId 用户id
     * @return 用户角色集合
     * @author chenjinlong
     * @since 2021/2/3 15:09
     */
    List<Long> findRoleIdsByUserId(Long userId);

    /**
     * 根据角色id找到角色对应的用户id集合
     *
     * @author fengshuonan
     * @since 2023/5/26 14:08
     */
    List<Long> findUserIdsByRoleId(Long roleId);

}
