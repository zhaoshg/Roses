package cn.stylefeng.roses.kernel.sys.modular.menu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.db.api.DbOperatorApi;
import cn.stylefeng.roses.kernel.event.sdk.publish.BusinessEventPublisher;
import cn.stylefeng.roses.kernel.rule.constants.SymbolConstant;
import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.rule.tree.buildpids.PidStructureBuildUtil;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveMenuCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.api.pojo.menu.UserAppMenuInfo;
import cn.stylefeng.roses.kernel.sys.modular.app.service.SysAppService;
import cn.stylefeng.roses.kernel.sys.modular.menu.constants.MenuConstants;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.enums.SysMenuExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.menu.factory.MenuFactory;
import cn.stylefeng.roses.kernel.sys.modular.menu.factory.MenuTreeFactory;
import cn.stylefeng.roses.kernel.sys.modular.menu.factory.MenuValidateFactory;
import cn.stylefeng.roses.kernel.sys.modular.menu.mapper.SysMenuMapper;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.response.AppGroupDetail;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统菜单业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Resource
    private SysAppService sysAppService;

    @Resource
    private DbOperatorApi dbOperatorApi;

    @Resource(name = "menuCodeCache")
    private CacheOperatorApi<String> menuCodeCache;

    @Override
    public void add(SysMenuRequest sysMenuRequest) {

        // 校验参数合法性
        MenuValidateFactory.validateAddMenuParam(sysMenuRequest);

        SysMenu sysMenu = new SysMenu();
        BeanUtil.copyProperties(sysMenuRequest, sysMenu);

        // 组装pids集合
        String pids = this.createPids(sysMenuRequest.getMenuParentId());
        sysMenu.setMenuPids(pids);

        this.save(sysMenu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(SysMenuRequest sysMenuRequest) {

        Long menuId = sysMenuRequest.getMenuId();

        // 获取所有子级的菜单id
        Set<Long> totalMenuIds = this.dbOperatorApi.findSubListByParentId("sys_menu", "menu_pids", "menu_id", menuId);
        totalMenuIds.add(menuId);

        // 删除菜单
        this.removeByIds(totalMenuIds);

        // 删除菜单下面关联的其他业务关联表
        Map<String, RemoveMenuCallbackApi> removeMenuCallbackApiMap = SpringUtil.getBeansOfType(RemoveMenuCallbackApi.class);
        for (RemoveMenuCallbackApi removeMenuCallbackApi : removeMenuCallbackApiMap.values()) {
            removeMenuCallbackApi.removeMenuAction(totalMenuIds);
        }

        // 发布菜单删除事件
        BusinessEventPublisher.publishEvent(MenuConstants.MENU_UPDATE_EVENT, menuId);

    }

    @Override
    public void edit(SysMenuRequest sysMenuRequest) {

        // 无法修改上下级结构（使用拖拽接口比修改接口更方便）
        sysMenuRequest.setMenuParentId(null);

        // 无法修改菜单的所属应用
        sysMenuRequest.setAppId(null);

        // 校验参数合法性
        MenuValidateFactory.validateAddMenuParam(sysMenuRequest);

        SysMenu sysMenu = this.querySysMenu(sysMenuRequest);
        BeanUtil.copyProperties(sysMenuRequest, sysMenu);

        this.updateById(sysMenu);

        // 发布菜单删除事件
        BusinessEventPublisher.publishEvent(MenuConstants.MENU_UPDATE_EVENT, sysMenu.getMenuId());

    }

    @Override
    public SysMenu detail(SysMenuRequest sysMenuRequest) {

        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysMenuLambdaQueryWrapper.eq(SysMenu::getMenuId, sysMenuRequest.getMenuId());

        sysMenuLambdaQueryWrapper.select(SysMenu::getAppId, SysMenu::getMenuName, SysMenu::getMenuCode, SysMenu::getMenuSort,
                SysMenu::getMenuType, SysMenu::getAntdvComponent, SysMenu::getAntdvRouter, SysMenu::getAntdvVisible,
                SysMenu::getAntdvActiveUrl, SysMenu::getAntdvLinkUrl, SysMenu::getAntdvIcon, SysMenu::getMenuParentId);

        SysMenu sysMenu = this.getOne(sysMenuLambdaQueryWrapper, false);

        if (ObjectUtil.isNotEmpty(sysMenu) && ObjectUtil.isNotEmpty(sysMenu.getMenuParentId())) {
            if (sysMenu.getMenuParentId().equals(TreeConstants.DEFAULT_PARENT_ID)) {
                sysMenu.setMenuParentName("根节点");
            } else {
                SysMenu parentMenu = this.getById(sysMenu.getMenuParentId());
                sysMenu.setMenuParentName(parentMenu.getMenuName());
            }
        }

        return sysMenu;
    }

    @Override
    public boolean validateMenuBindApp(Set<Long> appIdList) {

        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysMenuLambdaQueryWrapper.in(SysMenu::getAppId, appIdList);
        sysMenuLambdaQueryWrapper.select(SysMenu::getMenuId);
        long count = this.count(sysMenuLambdaQueryWrapper);

        return count > 0;
    }

    @Override
    public List<SysMenu> getTotalMenus() {
        LambdaQueryWrapper<SysMenu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.select(SysMenu::getMenuId, SysMenu::getMenuName, SysMenu::getMenuParentId, SysMenu::getAppId);
        menuLambdaQueryWrapper.orderByAsc(SysMenu::getMenuSort);
        return this.list(menuLambdaQueryWrapper);
    }

    @Override
    public List<SysMenu> getTotalMenus(List<Long> limitMenuIds) {
        if (ObjectUtil.isEmpty(limitMenuIds)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<SysMenu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.select(SysMenu::getMenuId, SysMenu::getMenuName, SysMenu::getMenuParentId, SysMenu::getAppId);
        menuLambdaQueryWrapper.in(SysMenu::getMenuId, limitMenuIds);
        menuLambdaQueryWrapper.orderByAsc(SysMenu::getMenuSort);
        return this.list(menuLambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenuTree(SysMenuRequest sysMenuRequest) {

        // 获取被更新的应用和菜单树信息
        List<SysMenu> updateTree = sysMenuRequest.getUpdateMenuTree();

        // 更新树节点的菜单顺序
        MenuTreeFactory.updateSort(updateTree, 1);

        // 填充树节点的parentId字段
        MenuTreeFactory.fillParentId(-1L, updateTree);

        // 平行展开树形结构，准备从新整理pids
        ArrayList<SysMenu> totalMenuList = new ArrayList<>();
        MenuTreeFactory.collectTreeTasks(updateTree, totalMenuList);

        // 从新整理上下级结构，整理id和pid关系
        PidStructureBuildUtil.createPidStructure(totalMenuList);

        // 更新菜单的sort字段、pid字段和pids字段这3个字段
        this.updateBatchById(totalMenuList);
    }

    @Override
    public List<SysMenu> getTotalMenuList() {
        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysMenuLambdaQueryWrapper.select(SysMenu::getMenuId, SysMenu::getAppId);
        return this.list(sysMenuLambdaQueryWrapper);
    }

    @Override
    public List<SysMenu> getIndexMenuInfoList(List<Long> menuIdList) {

        if (ObjectUtil.isEmpty(menuIdList)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();

        sysMenuLambdaQueryWrapper.in(SysMenu::getMenuId, menuIdList);

        // 查询指定的菜单内容
        sysMenuLambdaQueryWrapper.select(SysMenu::getMenuId, SysMenu::getMenuParentId, SysMenu::getMenuPids, SysMenu::getAppId,
                SysMenu::getMenuCode, SysMenu::getMenuName, SysMenu::getMenuType, SysMenu::getAntdvIcon, SysMenu::getAntdvVisible,
                SysMenu::getAntdvActiveUrl, SysMenu::getAntdvRouter, SysMenu::getAntdvComponent, SysMenu::getMenuSort);

        sysMenuLambdaQueryWrapper.orderByAsc(SysMenu::getMenuSort);

        return this.list(sysMenuLambdaQueryWrapper);
    }

    @Override
    public List<String> getMenuCodeList(List<Long> menuIdList) {

        List<String> result = new ArrayList<>();

        if (ObjectUtil.isEmpty(menuIdList)) {
            return result;
        }

        for (Long menuId : menuIdList) {

            String menuIdKey = menuId.toString();

            // 先从缓存查询，是否有菜单编码
            String menuCode = menuCodeCache.get(menuIdKey);

            if (ObjectUtil.isNotEmpty(menuCode)) {
                result.add(menuCode);
                continue;
            }

            // 查询库中的菜单编码
            LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysMenuLambdaQueryWrapper.eq(SysMenu::getMenuId, menuId);
            sysMenuLambdaQueryWrapper.select(SysMenu::getMenuCode);
            SysMenu sysMenu = this.getOne(sysMenuLambdaQueryWrapper, false);

            if (sysMenu != null) {
                String menuCodeQueryResult = sysMenu.getMenuCode();
                if (ObjectUtil.isNotEmpty(menuCodeQueryResult)) {

                    result.add(menuCodeQueryResult);

                    // 添加到缓存一份
                    menuCodeCache.put(menuIdKey, menuCodeQueryResult, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
                }
            }
        }

        return result;
    }

    @Override
    public List<AppGroupDetail> getAppMenuGroupDetail(SysMenuRequest sysMenuRequest) {

        // 1. 获取所有应用列表
        List<AppGroupDetail> appList = sysAppService.getAppList();
        if (ObjectUtil.isEmpty(appList)) {
            return new ArrayList<>();
        }

        // 所有的应用id
        Set<Long> totalAppIds = appList.stream().map(AppGroupDetail::getAppId).collect(Collectors.toSet());

        // 2. 获取应用对应的所有菜单
        LambdaQueryWrapper<SysMenu> wrapper = this.createWrapper(sysMenuRequest);
        wrapper.in(SysMenu::getAppId, totalAppIds);
        wrapper.select(SysMenu::getMenuId, SysMenu::getMenuParentId, SysMenu::getMenuPids, SysMenu::getMenuName, SysMenu::getAppId,
                SysMenu::getMenuType);
        List<SysMenu> sysMenuList = this.list(wrapper);
        if (ObjectUtil.isEmpty(sysMenuList)) {
            return appList;
        }

        // 2.1 如果查询条件不为空，则需要补全被查询菜单的父级结构，否则组不成树结构
        if (StrUtil.isNotBlank(sysMenuRequest.getSearchText())) {
            Set<Long> menuParentIds = MenuFactory.getMenuParentIds(sysMenuList);
            if (ObjectUtil.isNotEmpty(menuParentIds)) {
                LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(SysMenu::getMenuId, menuParentIds);
                queryWrapper.select(SysMenu::getMenuId, SysMenu::getMenuParentId, SysMenu::getMenuPids, SysMenu::getMenuName,
                        SysMenu::getAppId, SysMenu::getMenuType);
                queryWrapper.orderByAsc(SysMenu::getMenuSort);
                List<SysMenu> parentMenus = this.list(queryWrapper);
                sysMenuList.addAll(parentMenus);
            }
        }

        // 3. 组装应用信息和菜单信息
        return MenuFactory.createAppGroupDetailResult(appList, sysMenuList);
    }

    @Override
    public List<UserAppMenuInfo> getUserAppMenuDetailList(Set<Long> menuIdList) {

        // 通过id查询菜单的详情信息
        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysMenuLambdaQueryWrapper.in(SysMenu::getMenuId, menuIdList);
        sysMenuLambdaQueryWrapper.select(SysMenu::getMenuId, SysMenu::getMenuName, SysMenu::getAntdvRouter, SysMenu::getAntdvIcon);
        List<SysMenu> sysMenuList = this.list(sysMenuLambdaQueryWrapper);

        if (ObjectUtil.isEmpty(sysMenuList)) {
            return new ArrayList<>();
        }

        // 转化成响应信息
        List<UserAppMenuInfo> result = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            UserAppMenuInfo userAppMenuInfo = new UserAppMenuInfo();
            userAppMenuInfo.setMenuId(sysMenu.getMenuId());
            userAppMenuInfo.setMenuName(sysMenu.getMenuName());
            userAppMenuInfo.setMenuIcon(sysMenu.getAntdvIcon());
            userAppMenuInfo.setMenuRouter(sysMenu.getAntdvRouter());
            result.add(userAppMenuInfo);
        }

        return result;
    }

    @Override
    public Map<Long, Long> getMenuAppId(List<Long> menuIdList) {

        // 定义返回结果
        HashMap<Long, Long> menuIdAppIdMap = new HashMap<>();

        if (ObjectUtil.isEmpty(menuIdList)) {
            return menuIdAppIdMap;
        }

        // 查询数据库菜单id对应的应用id集合
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysMenu::getMenuId, menuIdList);
        queryWrapper.select(SysMenu::getAppId, SysMenu::getMenuId);
        List<SysMenu> queryList = this.list(queryWrapper);

        if (ObjectUtil.isEmpty(queryList)) {
            return menuIdAppIdMap;
        }

        // 制作映射关系
        for (SysMenu sysMenu : queryList) {
            menuIdAppIdMap.put(sysMenu.getMenuId(), sysMenu.getAppId());
        }

        return menuIdAppIdMap;
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    private SysMenu querySysMenu(SysMenuRequest sysMenuRequest) {
        SysMenu sysMenu = this.getById(sysMenuRequest.getMenuId());
        if (ObjectUtil.isEmpty(sysMenu)) {
            throw new ServiceException(SysMenuExceptionEnum.SYS_MENU_NOT_EXISTED);
        }
        return sysMenu;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    private LambdaQueryWrapper<SysMenu> createWrapper(SysMenuRequest sysMenuRequest) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();

        // 根据搜索条件查询菜单，根据菜单名称、编码、路由地址、组件地址
        String searchText = sysMenuRequest.getSearchText();
        if (StrUtil.isNotBlank(searchText)) {
            queryWrapper.like(SysMenu::getMenuName, searchText);
            queryWrapper.or().like(SysMenu::getMenuCode, searchText);
            queryWrapper.or().like(SysMenu::getAntdvRouter, searchText);
            queryWrapper.or().like(SysMenu::getAntdvComponent, searchText);
        }

        // 根据顺序排序
        queryWrapper.orderByAsc(SysMenu::getMenuSort);

        return queryWrapper;
    }

    /**
     * 创建pids的值
     * <p>
     * 如果pid是顶级节点，pids = 【[-1],】
     * <p>
     * 如果pid不是顶级节点，pids = 【父菜单的pids,[pid],】
     *
     * @author fengshuonan
     * @since 2023/6/15 10:09
     */
    private String createPids(Long pid) {
        if (pid.equals(TreeConstants.DEFAULT_PARENT_ID)) {
            return SymbolConstant.LEFT_SQUARE_BRACKETS + TreeConstants.DEFAULT_PARENT_ID + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA;
        } else {
            // 获取父菜单
            LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysMenuLambdaQueryWrapper.eq(SysMenu::getMenuId, pid);
            sysMenuLambdaQueryWrapper.select(SysMenu::getMenuPids);
            SysMenu parentMenu = this.getOne(sysMenuLambdaQueryWrapper, false);
            if (parentMenu == null) {
                return SymbolConstant.LEFT_SQUARE_BRACKETS + TreeConstants.DEFAULT_PARENT_ID + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA;
            } else {
                // 组装pids
                return parentMenu.getMenuPids() + SymbolConstant.LEFT_SQUARE_BRACKETS + pid + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA;
            }
        }
    }
}