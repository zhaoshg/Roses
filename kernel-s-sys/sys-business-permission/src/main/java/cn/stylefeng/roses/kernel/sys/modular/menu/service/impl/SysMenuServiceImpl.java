package cn.stylefeng.roses.kernel.sys.modular.menu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.enums.SysMenuExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.menu.mapper.SysMenuMapper;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.response.AppGroupDetail;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 系统菜单业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

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
        LambdaQueryWrapper<SysMenu> wrapper = this.createWrapper(sysMenuRequest);
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

        Long menuId = sysMenuRequest.getMenuId();
        Long menuParentId = sysMenuRequest.getMenuParentId();
        String menuPids = sysMenuRequest.getMenuPids();
        String menuName = sysMenuRequest.getMenuName();
        String menuCode = sysMenuRequest.getMenuCode();
        Long appId = sysMenuRequest.getAppId();
        BigDecimal menuSort = sysMenuRequest.getMenuSort();
        Integer statusFlag = sysMenuRequest.getStatusFlag();
        String remark = sysMenuRequest.getRemark();
        Integer menuType = sysMenuRequest.getMenuType();
        String antdvRouter = sysMenuRequest.getAntdvRouter();
        String antdvComponent = sysMenuRequest.getAntdvComponent();
        String antdvIcon = sysMenuRequest.getAntdvIcon();
        String antdvLinkUrl = sysMenuRequest.getAntdvLinkUrl();
        String antdvActiveUrl = sysMenuRequest.getAntdvActiveUrl();
        String antdvVisible = sysMenuRequest.getAntdvVisible();
        String expandField = sysMenuRequest.getExpandField();
        Long versionFlag = sysMenuRequest.getVersionFlag();
        String delFlag = sysMenuRequest.getDelFlag();
        Long tenantId = sysMenuRequest.getTenantId();

        queryWrapper.eq(ObjectUtil.isNotNull(menuId), SysMenu::getMenuId, menuId);
        queryWrapper.eq(ObjectUtil.isNotNull(menuParentId), SysMenu::getMenuParentId, menuParentId);
        queryWrapper.like(ObjectUtil.isNotEmpty(menuPids), SysMenu::getMenuPids, menuPids);
        queryWrapper.like(ObjectUtil.isNotEmpty(menuName), SysMenu::getMenuName, menuName);
        queryWrapper.like(ObjectUtil.isNotEmpty(menuCode), SysMenu::getMenuCode, menuCode);
        queryWrapper.eq(ObjectUtil.isNotNull(appId), SysMenu::getAppId, appId);
        queryWrapper.eq(ObjectUtil.isNotNull(menuSort), SysMenu::getMenuSort, menuSort);
        queryWrapper.eq(ObjectUtil.isNotNull(statusFlag), SysMenu::getStatusFlag, statusFlag);
        queryWrapper.like(ObjectUtil.isNotEmpty(remark), SysMenu::getRemark, remark);
        queryWrapper.eq(ObjectUtil.isNotNull(menuType), SysMenu::getMenuType, menuType);
        queryWrapper.like(ObjectUtil.isNotEmpty(antdvRouter), SysMenu::getAntdvRouter, antdvRouter);
        queryWrapper.like(ObjectUtil.isNotEmpty(antdvComponent), SysMenu::getAntdvComponent, antdvComponent);
        queryWrapper.like(ObjectUtil.isNotEmpty(antdvIcon), SysMenu::getAntdvIcon, antdvIcon);
        queryWrapper.like(ObjectUtil.isNotEmpty(antdvLinkUrl), SysMenu::getAntdvLinkUrl, antdvLinkUrl);
        queryWrapper.like(ObjectUtil.isNotEmpty(antdvActiveUrl), SysMenu::getAntdvActiveUrl, antdvActiveUrl);
        queryWrapper.like(ObjectUtil.isNotEmpty(antdvVisible), SysMenu::getAntdvVisible, antdvVisible);
        queryWrapper.like(ObjectUtil.isNotEmpty(expandField), SysMenu::getExpandField, expandField);
        queryWrapper.eq(ObjectUtil.isNotNull(versionFlag), SysMenu::getVersionFlag, versionFlag);
        queryWrapper.like(ObjectUtil.isNotEmpty(delFlag), SysMenu::getDelFlag, delFlag);
        queryWrapper.eq(ObjectUtil.isNotNull(tenantId), SysMenu::getTenantId, tenantId);

        return queryWrapper;
    }

}