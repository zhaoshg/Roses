package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleAssignOperateAction;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionItem;

import java.util.List;

/**
 * 角色绑定权限，点击绑定应用时候的业务处理
 *
 * @author fengshuonan
 * @since 2023/6/14 14:13
 */
public class RoleBindAppImpl implements RoleAssignOperateAction {

    @Override
    public PermissionNodeTypeEnum getNodeType() {
        return PermissionNodeTypeEnum.APP;
    }

    @Override
    public List<RoleBindPermissionItem> doOperateAction(RoleBindPermissionRequest roleBindPermissionRequest) {
        return null;
    }

}
