package cn.stylefeng.roses.kernel.sys.modular.user.service;

import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.NewUserRoleBindResponse;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request.DeleteRequest;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request.RoleControlRequest;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request.StatusControlRequest;

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

    /**
     * 修改用户绑定机构状态`
     *
     * @author fengshuonan
     * @since 2024-01-18 9:34
     */
    void changeUserBindStatus(StatusControlRequest statusControlRequest);

    /**
     * 设置角色的选中状态的填充
     *
     * @author fengshuonan
     * @since 2024-01-18 10:50
     */
    void changeRoleSelect(RoleControlRequest roleControlRequest);

    /**
     * 删除机构的绑定
     *
     * @author fengshuonan
     * @since 2024-01-18 13:39
     */
    void removeUserOrgBind(DeleteRequest deleteRequest);

}