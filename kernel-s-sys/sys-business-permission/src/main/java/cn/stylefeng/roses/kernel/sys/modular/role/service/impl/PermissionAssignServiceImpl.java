package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.stylefeng.roses.kernel.sys.modular.app.entity.SysApp;
import cn.stylefeng.roses.kernel.sys.modular.app.service.SysAppService;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.factory.PermissionAssignFactory;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionItem;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionResponse;
import cn.stylefeng.roses.kernel.sys.modular.role.service.PermissionAssignService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色权限绑定相关的接口
 *
 * @author fengshuonan
 * @since 2023/6/13 16:14
 */
public class PermissionAssignServiceImpl implements PermissionAssignService {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysAppService sysAppService;

    @Override
    public RoleBindPermissionResponse getRoleBindPermission(RoleBindPermissionRequest roleBindPermissionRequest) {
        // 1. 整理出一个总的响应的结构树，选择状态为空


        // 2. 获取角色绑定的应用，菜单，功能列表


        // 3. 组合结构树和角色绑定的信息，填充选择状态，封装返回结果

        return null;
    }

    @Override
    public RoleBindPermissionResponse createSelectTreeStructure() {

        // 最顶层，代表是否全选
        RoleBindPermissionResponse roleBindPermissionResponse = new RoleBindPermissionResponse();
        roleBindPermissionResponse.setChecked(false);

        // 获取所有的菜单
        LambdaQueryWrapper<SysMenu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.select(SysMenu::getMenuId, SysMenu::getMenuName, SysMenu::getMenuParentId, SysMenu::getAppId);
        menuLambdaQueryWrapper.orderByAsc(SysMenu::getMenuSort);
        List<SysMenu> totalMenus = this.sysMenuService.list(menuLambdaQueryWrapper);

        // 组装所有的叶子节点菜单
        List<RoleBindPermissionItem> totalResultMenus = PermissionAssignFactory.createPermissionMenus(totalMenus);

        // 查询菜单对应的所有应用
        Set<String> appIdList = totalResultMenus.stream().map(RoleBindPermissionItem::getNodeParentId).collect(Collectors.toSet());
        LambdaQueryWrapper<SysApp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SysApp::getAppId, SysApp::getAppName);
        List<SysApp> totalAppList = sysAppService.list(queryWrapper);

        // 组装所有的应用节点信息



        // 获取所有的菜单上的功能


        // 获取菜单对应的应用


        return null;
    }

}
