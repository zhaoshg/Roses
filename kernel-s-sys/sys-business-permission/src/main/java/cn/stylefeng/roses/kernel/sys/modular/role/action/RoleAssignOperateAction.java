package cn.stylefeng.roses.kernel.sys.modular.role.action;

import cn.stylefeng.roses.kernel.sys.modular.role.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionItem;

import java.util.List;

/**
 * 角色绑定权限操作的接口
 *
 * @author fengshuonan
 * @since 2023/6/13 22:16
 */
public interface RoleAssignOperateAction {

    /**
     * 获取节点的类型，有4种节点类型
     *
     * @author fengshuonan
     * @since 2023/6/13 22:17
     */
    PermissionNodeTypeEnum getNodeType();

    /**
     * 执行角色绑定权限的过程，根据不同的点击类型，执行不同的操作过程
     *
     * @param roleBindPermissionRequest 角色绑定权限的参数
     * @return 返回节点绑定后的执行结果，返回的是当前操作节点的子集的所有
     * @author fengshuonan
     * @since 2023/6/13 22:17
     */
    List<RoleBindPermissionItem> doOperateAction(RoleBindPermissionRequest roleBindPermissionRequest);

}
