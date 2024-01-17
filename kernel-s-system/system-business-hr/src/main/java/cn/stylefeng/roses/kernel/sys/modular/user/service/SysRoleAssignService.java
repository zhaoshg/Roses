package cn.stylefeng.roses.kernel.sys.modular.user.service;

import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.NewUserRoleBindResponse;

import java.util.List;

/**
 * 新的用户授权界面业务
 *
 * @author fengshuonan
 * @since 2024/1/17 23:08
 */
public interface SysRoleAssignService {

    /**
     * 获取用户的角色授权信息列表
     *
     * @author fengshuonan
     * @since 2024/1/17 23:09
     */
    List<NewUserRoleBindResponse> getUserAssignList(Long userId);

}