package cn.stylefeng.roses.kernel.sys.modular.menu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.app.service.SysAppService;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.enums.SysMenuExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.menu.factory.MenuFactory;
import cn.stylefeng.roses.kernel.sys.modular.menu.mapper.SysMenuMapper;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.response.AppGroupDetail;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    @Override
    public void add(SysMenuRequest sysMenuRequest) {
        SysMenu sysMenu = new SysMenu();
        BeanUtil.copyProperties(sysMenuRequest, sysMenu);
        this.save(sysMenu);
    }

    @Override
    public void del(SysMenuRequest sysMenuRequest) {
        SysMenu sysMenu = this.querySysMenu(sysMenuRequest);
        this.removeById(sysMenu.getMenuId());
    }

    @Override
    public void edit(SysMenuRequest sysMenuRequest) {
        SysMenu sysMenu = this.querySysMenu(sysMenuRequest);
        BeanUtil.copyProperties(sysMenuRequest, sysMenu);
        this.updateById(sysMenu);
    }

    @Override
    public SysMenu detail(SysMenuRequest sysMenuRequest) {
        return this.querySysMenu(sysMenuRequest);
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
    public Long getMenuAppId(Long menuId) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getMenuId, menuId);
        queryWrapper.select(SysMenu::getAppId);
        SysMenu one = this.getOne(queryWrapper, false);
        if (one != null) {
            return one.getAppId();
        } else {
            return null;
        }
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
        wrapper.select(SysMenu::getMenuId, SysMenu::getMenuParentId, SysMenu::getMenuPids, SysMenu::getMenuName, SysMenu::getAppId, SysMenu::getMenuType);
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
                queryWrapper.select(SysMenu::getMenuId, SysMenu::getMenuParentId, SysMenu::getMenuPids, SysMenu::getMenuName, SysMenu::getAppId, SysMenu::getMenuType);
                queryWrapper.orderByAsc(SysMenu::getMenuSort);
                List<SysMenu> parentMenus = this.list(queryWrapper);
                sysMenuList.addAll(parentMenus);
            }
        }

        // 3. 组装应用信息和菜单信息
        return new ArrayList<>();
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

}