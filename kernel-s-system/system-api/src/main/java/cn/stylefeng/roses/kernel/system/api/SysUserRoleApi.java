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
     * @date 2021/2/3 15:09
     */
    List<Long> findRoleIdsByUserId(Long userId);
}
