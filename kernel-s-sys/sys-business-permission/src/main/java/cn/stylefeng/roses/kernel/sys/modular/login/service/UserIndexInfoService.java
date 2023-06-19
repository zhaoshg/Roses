package cn.stylefeng.roses.kernel.sys.modular.login.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.auth.api.SessionManagerApi;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.rule.tree.factory.DefaultTreeBuildFactory;
import cn.stylefeng.roses.kernel.sys.api.SysUserOrgServiceApi;
import cn.stylefeng.roses.kernel.sys.api.SysUserRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.SimpleUserDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserOrgDTO;
import cn.stylefeng.roses.kernel.sys.modular.app.service.SysAppService;
import cn.stylefeng.roses.kernel.sys.modular.login.expander.WebSocketConfigExpander;
import cn.stylefeng.roses.kernel.sys.modular.login.pojo.IndexUserAppInfo;
import cn.stylefeng.roses.kernel.sys.modular.login.pojo.IndexUserMenuInfo;
import cn.stylefeng.roses.kernel.sys.modular.login.pojo.IndexUserOrgInfo;
import cn.stylefeng.roses.kernel.sys.modular.login.pojo.UserIndexInfo;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 获取用户首页信息的业务
 *
 * @author fengshuonan
 * @since 2023/6/18 22:55
 */
@Service
public class UserIndexInfoService {

    @Resource
    private SysUserServiceApi sysUserServiceApi;

    @Resource
    private SysUserOrgServiceApi sysUserOrgServiceApi;

    @Resource
    private SysUserRoleServiceApi sysUserRoleServiceApi;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysMenuOptionsService sysMenuOptionsService;

    @Resource
    private SysAppService sysAppService;

    @Resource
    private SessionManagerApi sessionManagerApi;

    /**
     * 获取用户首页信息
     *
     * @author fengshuonan
     * @since 2023/6/18 22:55
     */
    public UserIndexInfo getUserIndexInfo() {

        // 返回结果初始化
        UserIndexInfo userIndexInfo = new UserIndexInfo();

        // 获取当前登录用户
        LoginUser loginUser = LoginContext.me().getLoginUser();

        // 1. 获取用户的姓名和头像
        this.fillUserBaseInfo(loginUser.getUserId(), userIndexInfo);

        // 2. 获取用户所有的部门和任职信息，并默认激活（选中）主部门的任职信息
        this.fillUserOrgInfo(loginUser, userIndexInfo);

        // 3. 获取用户的权限编码集合
        ArrayList<SysMenu> userMenuList = new ArrayList<>();
        this.fillUserPermissionCodeList(loginUser, userIndexInfo, userMenuList);

        // 4. 获取用户的当前登录App
        this.fillUserAppList(loginUser, userIndexInfo, userMenuList);

        // 5. 填充用户的菜单信息
        this.fillUserMenuList(loginUser, userIndexInfo, userMenuList);

        // 6. 获取菜单和路由的appId映射关系【用来给前端作为是否切换应用的依据】
        this.fillMenuUrlAppIdMap(userIndexInfo, userMenuList);

        // 7. 构建websocket url
        this.fillWebSocketUrl(loginUser, userIndexInfo);

        // 8. 更新用户的session信息，因为可能更新了loginUser中的值
        sessionManagerApi.updateSession(loginUser.getToken(), loginUser);

        return userIndexInfo;
    }

    /**
     * 填充用户的基本姓名和头像信息
     *
     * @author fengshuonan
     * @since 2023/6/18 23:01
     */
    private void fillUserBaseInfo(Long userId, UserIndexInfo userIndexInfo) {
        SimpleUserDTO simpleUserDTO = sysUserServiceApi.getUserInfoByUserId(userId);
        userIndexInfo.setUserId(simpleUserDTO.getUserId());
        userIndexInfo.setRealName(simpleUserDTO.getRealName());
        userIndexInfo.setAvatarUrl(simpleUserDTO.getAvatarUrl());
    }

    /**
     * 获取用户的任职信息，包含了机构的详细描述
     * <p>
     * 如果指定用户没有当前激活的组织机构id，则直接指定默认的组织机构
     * <p>
     * 用在首页获取用户详情接口
     *
     * @author fengshuonan
     * @since 2023/6/18 23:33
     */
    private void fillUserOrgInfo(LoginUser loginUser, UserIndexInfo userIndexInfo) {

        List<IndexUserOrgInfo> resultUserOrg = new ArrayList<>();

        // 获取当前用户是否有已经激活的公司id
        Long currentOrgId = loginUser.getCurrentOrgId();

        // 获取用户的所有组织机构集合
        List<UserOrgDTO> userOrgList = sysUserOrgServiceApi.getUserOrgList(loginUser.getUserId());

        // 查询到机构为空，直接返回
        if (ObjectUtil.isEmpty(userOrgList)) {
            userIndexInfo.setUserOrgInfoList(resultUserOrg);
            return;
        }

        // 将查询结果转化为前端需要的信息
        for (UserOrgDTO userOrgItem : userOrgList) {
            IndexUserOrgInfo indexUserOrgInfo = new IndexUserOrgInfo();
            indexUserOrgInfo.setUserId(loginUser.getUserId());

            // 设置当前orgId为部门id，如果部门id为空，则代表当前用户只绑定到公司id了
            if (ObjectUtil.isNotEmpty(userOrgItem.getDeptId())) {
                indexUserOrgInfo.setOrgId(userOrgItem.getDeptId());
            } else {
                indexUserOrgInfo.setOrgId(userOrgItem.getCompanyId());
            }

            indexUserOrgInfo.setCompanyName(userOrgItem.getCompanyName());
            indexUserOrgInfo.setDeptName(userOrgItem.getDeptName());
            indexUserOrgInfo.setPositionName(userOrgItem.getPositionName());

            // 设置是否是主要任职部门
            indexUserOrgInfo.setMainFlag(YesOrNotEnum.Y.getCode().equals(userOrgItem.getMainFlag()));

            resultUserOrg.add(indexUserOrgInfo);
        }

        // 设置用户的选中的组织机构
        // 如果当前没激活的组织机构，则直接将主部门激活
        if (currentOrgId == null) {
            for (IndexUserOrgInfo indexUserOrgInfo : resultUserOrg) {
                indexUserOrgInfo.setCurrentSelectFlag(indexUserOrgInfo.getMainFlag());

                // 更新用户的当前组织机构id
                if (indexUserOrgInfo.getMainFlag()) {
                    loginUser.setCurrentOrgId(indexUserOrgInfo.getOrgId());
                }
            }
        }
        // 如果有激活的组织机构
        else {
            for (IndexUserOrgInfo indexUserOrgInfo : resultUserOrg) {
                indexUserOrgInfo.setCurrentSelectFlag(indexUserOrgInfo.getOrgId().equals(currentOrgId));
            }
        }

        // 填充用户组织机构信息
        userIndexInfo.setUserOrgInfoList(resultUserOrg);
    }

    /**
     * 填充用户的权限编码集合
     *
     * @author fengshuonan
     * @since 2023/6/19 12:38
     */
    private void fillUserPermissionCodeList(LoginUser loginUser, UserIndexInfo userIndexInfo, List<SysMenu> userMenuList) {

        Long userId = loginUser.getUserId();

        // 获取用户的角色集合
        List<Long> roleIdList = sysUserRoleServiceApi.getUserRoleIdList(userId);

        if (ObjectUtil.isEmpty(roleIdList)) {
            userIndexInfo.setPermissionCodeList(new HashSet<>());
            return;
        }

        // 获取角色对应的菜单id和菜单功能id
        List<Long> menuIdList = sysRoleMenuService.getRoleBindMenuIdList(roleIdList);
        List<Long> menuOptionsIdList = sysRoleMenuOptionsService.getRoleBindMenuOptionsIdList(roleIdList);

        HashSet<String> permissionCodeList = new HashSet<>();

        // 获取菜单对应的菜单编码集合
        userMenuList = sysMenuService.getIndexMenuInfoList(menuIdList);
        Set<String> menuCodeList = userMenuList.stream().map(SysMenu::getMenuCode).collect(Collectors.toSet());
        permissionCodeList.addAll(menuCodeList);

        // 获取功能对应的功能编码集合
        List<String> optionsCodeList = sysMenuOptionsService.getOptionsCodeList(menuOptionsIdList);
        permissionCodeList.addAll(optionsCodeList);

        userIndexInfo.setPermissionCodeList(permissionCodeList);
    }

    /**
     * 填充当前用户的应用列表和菜单信息
     *
     * @author fengshuonan
     * @since 2023/6/19 22:25
     */
    private void fillUserAppList(LoginUser loginUser, UserIndexInfo userIndexInfo, List<SysMenu> userMenuList) {

        if (ObjectUtil.isEmpty(userMenuList)) {
            return;
        }

        // 先统计用户有多少个应用
        Set<Long> userAppIds = userMenuList.stream().map(SysMenu::getAppId).collect(Collectors.toSet());

        // 查询这些应用的应用名称，并且按应用的顺序进行排序
        List<IndexUserAppInfo> indexUserAppList = sysAppService.getIndexUserAppList(userAppIds);

        // 判断当前用户是否有绑定的应用id
        // 如果没绑定的，则默认选中第一个
        if (ObjectUtil.isEmpty(loginUser.getCurrentAppId())) {
            indexUserAppList.get(0).setCurrentSelectFlag(true);
            // 更新用户的当前应用id
            loginUser.setCurrentAppId(indexUserAppList.get(0).getAppId());
        } else {
            for (IndexUserAppInfo indexUserAppInfo : indexUserAppList) {
                if (indexUserAppInfo.getAppId().equals(loginUser.getCurrentAppId())) {
                    indexUserAppInfo.setCurrentSelectFlag(true);
                }
            }
        }

        userIndexInfo.setUserAppInfoList(indexUserAppList);
    }

    /**
     * 填充用户的当前菜单信息
     *
     * @author fengshuonan
     * @since 2023/6/19 22:38
     */
    private void fillUserMenuList(LoginUser loginUser, UserIndexInfo userIndexInfo, List<SysMenu> userMenuList) {

        // 获取用户当前激活的应用
        Long currentAppId = loginUser.getCurrentAppId();

        // 从用户菜单中筛选这个应用下的所有菜单
        List<SysMenu> sysUserMenus = userMenuList.stream().filter(i -> i.getAppId().equals(currentAppId)).collect(Collectors.toList());

        // 菜单信息转化为首页需要的菜单信息
        ArrayList<IndexUserMenuInfo> userIndexMenus = new ArrayList<>();
        for (SysMenu menuItem : sysUserMenus) {
            IndexUserMenuInfo indexUserMenuInfo = new IndexUserMenuInfo();
            indexUserMenuInfo.setMenuId(menuItem.getMenuId());
            indexUserMenuInfo.setMenuParentId(menuItem.getMenuParentId());
            indexUserMenuInfo.setTitle(menuItem.getMenuName());
            indexUserMenuInfo.setIcon(menuItem.getAntdvIcon());
            indexUserMenuInfo.setHide(YesOrNotEnum.N.getCode().equals(menuItem.getAntdvVisible()));
            indexUserMenuInfo.setActive(menuItem.getAntdvActiveUrl());
            indexUserMenuInfo.setPath(menuItem.getAntdvRouter());
            indexUserMenuInfo.setComponent(menuItem.getAntdvComponent());
            indexUserMenuInfo.setSortNumber(menuItem.getMenuSort());
            userIndexMenus.add(indexUserMenuInfo);
        }

        // 构造树形结构
        List<IndexUserMenuInfo> indexUserMenuInfos = new DefaultTreeBuildFactory<IndexUserMenuInfo>().doTreeBuild(userIndexMenus);
        userIndexInfo.setMenuList(indexUserMenuInfos);
    }

    /**
     * 填充菜单和应用id的映射
     *
     * @author fengshuonan
     * @since 2023/6/19 23:02
     */
    private void fillMenuUrlAppIdMap(UserIndexInfo userIndexInfo, List<SysMenu> userMenuList) {

        // 菜单路由和appId的映射关系
        HashMap<String, Long> menuUrlAppIdMap = new HashMap<>();

        for (SysMenu sysMenu : userMenuList) {
            String antdvRouter = sysMenu.getAntdvRouter();
            Long appId = sysMenu.getAppId();
            menuUrlAppIdMap.put(antdvRouter, appId);
        }

        userIndexInfo.setMenuUrlAppIdMap(menuUrlAppIdMap);
    }

    /**
     * 填充用户的websocket url
     *
     * @author fengshuonan
     * @since 2023/6/19 23:13
     */
    private void fillWebSocketUrl(LoginUser loginUser, UserIndexInfo userIndexInfo) {

        // 从sys_config中获取配置
        String webSocketWsUrl = WebSocketConfigExpander.getWebSocketWsUrl();
        Map<String, String> params = new HashMap<>(1);
        params.put("token", loginUser.getToken());
        webSocketWsUrl = StrUtil.format(webSocketWsUrl, params);

        userIndexInfo.setWebsocketUrl(webSocketWsUrl);
    }

}
