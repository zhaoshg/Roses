package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionResponse;
import cn.stylefeng.roses.kernel.sys.modular.role.service.PermissionAssignService;

/**
 * 角色权限绑定相关的接口
 *
 * @author fengshuonan
 * @since 2023/6/13 16:14
 */
public class PermissionAssignServiceImpl implements PermissionAssignService {


    @Override
    public RoleBindPermissionResponse getRoleBindPermission(RoleBindPermissionRequest roleBindPermissionRequest) {
        // 1. 整理出一个总的响应的结构树，选择状态为空


        // 2. 获取角色绑定的应用，菜单，功能列表


        // 3. 组合结构树和角色绑定的信息，填充选择状态，封装返回结果

        return null;
    }

    @Override
    public RoleBindPermissionResponse createSelectTreeStructure() {
        return null;
    }

}
