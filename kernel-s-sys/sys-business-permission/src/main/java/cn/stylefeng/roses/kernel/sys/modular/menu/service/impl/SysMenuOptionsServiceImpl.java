package cn.stylefeng.roses.kernel.sys.modular.menu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveMenuCallbackApi;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.menu.enums.SysMenuOptionsExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.menu.factory.MenuOptionsValidateFactory;
import cn.stylefeng.roses.kernel.sys.modular.menu.mapper.SysMenuOptionsMapper;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuOptionsRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 菜单下的功能操作业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@Service
public class SysMenuOptionsServiceImpl extends ServiceImpl<SysMenuOptionsMapper, SysMenuOptions> implements SysMenuOptionsService,
        RemoveMenuCallbackApi {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    @Override
    public void add(SysMenuOptionsRequest sysMenuOptionsRequest) {

        // 同菜单下功能名称和编码不能重复
        MenuOptionsValidateFactory.validateMenuOptionsParam(sysMenuOptionsRequest);

        SysMenuOptions sysMenuOptions = new SysMenuOptions();
        BeanUtil.copyProperties(sysMenuOptionsRequest, sysMenuOptions);

        // 获取菜单的应用id
        Map<Long, Long> menuAppId = sysMenuService.getMenuAppId(ListUtil.list(false, sysMenuOptionsRequest.getMenuId()));
        Long appId = menuAppId.get(sysMenuOptionsRequest.getMenuId());
        sysMenuOptions.setAppId(appId);

        this.save(sysMenuOptions);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(SysMenuOptionsRequest sysMenuOptionsRequest) {

        SysMenuOptions sysMenuOptions = this.querySysMenuOptions(sysMenuOptionsRequest);
        this.removeById(sysMenuOptions.getMenuOptionId());

        // 删除角色绑定的功能关联
        sysRoleMenuOptionsService.removeRoleBindOptions(sysMenuOptionsRequest.getMenuOptionId());
    }

    @Override
    public void edit(SysMenuOptionsRequest sysMenuOptionsRequest) {

        // 同菜单下功能名称和编码不能重复
        MenuOptionsValidateFactory.validateMenuOptionsParam(sysMenuOptionsRequest);

        SysMenuOptions sysMenuOptions = this.querySysMenuOptions(sysMenuOptionsRequest);
        BeanUtil.copyProperties(sysMenuOptionsRequest, sysMenuOptions);
        this.updateById(sysMenuOptions);
    }

    @Override
    public SysMenuOptions detail(SysMenuOptionsRequest sysMenuOptionsRequest) {
        return this.querySysMenuOptions(sysMenuOptionsRequest);
    }

    @Override
    public PageResult<SysMenuOptions> findPage(SysMenuOptionsRequest sysMenuOptionsRequest) {
        LambdaQueryWrapper<SysMenuOptions> wrapper = createWrapper(sysMenuOptionsRequest);

        // 只查询有用字段
        wrapper.select(SysMenuOptions::getOptionName, SysMenuOptions::getOptionCode, SysMenuOptions::getMenuId,
                SysMenuOptions::getMenuOptionId);

        Page<SysMenuOptions> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysMenuOptions> getTotalMenuOptionsList() {
        LambdaQueryWrapper<SysMenuOptions> sysMenuOptionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysMenuOptionsLambdaQueryWrapper.select(SysMenuOptions::getMenuOptionId, SysMenuOptions::getMenuId, SysMenuOptions::getAppId);
        return this.list(sysMenuOptionsLambdaQueryWrapper);
    }

    @Override
    public List<String> getOptionsCodeList(List<Long> optionsIdList) {

        if (ObjectUtil.isEmpty(optionsIdList)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysMenuOptions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysMenuOptions::getMenuOptionId, optionsIdList);
        queryWrapper.select(SysMenuOptions::getOptionCode);
        List<SysMenuOptions> sysMenuOptionsList = this.list(queryWrapper);

        if (ObjectUtil.isEmpty(sysMenuOptionsList)) {
            return new ArrayList<>();
        }

        return sysMenuOptionsList.stream().map(SysMenuOptions::getOptionCode).collect(Collectors.toList());
    }

    @Override
    public List<SysMenuOptions> findList(SysMenuOptionsRequest sysMenuOptionsRequest) {
        LambdaQueryWrapper<SysMenuOptions> wrapper = this.createWrapper(sysMenuOptionsRequest);
        return this.list(wrapper);
    }

    @Override
    public void removeMenuAction(Set<Long> beRemovedMenuIdList) {
        LambdaQueryWrapper<SysMenuOptions> menuOptionsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuOptionsLambdaQueryWrapper.in(SysMenuOptions::getMenuId, beRemovedMenuIdList);
        this.remove(menuOptionsLambdaQueryWrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    private SysMenuOptions querySysMenuOptions(SysMenuOptionsRequest sysMenuOptionsRequest) {
        SysMenuOptions sysMenuOptions = this.getById(sysMenuOptionsRequest.getMenuOptionId());
        if (ObjectUtil.isEmpty(sysMenuOptions)) {
            throw new ServiceException(SysMenuOptionsExceptionEnum.SYS_MENU_OPTIONS_NOT_EXISTED);
        }
        return sysMenuOptions;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    private LambdaQueryWrapper<SysMenuOptions> createWrapper(SysMenuOptionsRequest sysMenuOptionsRequest) {
        LambdaQueryWrapper<SysMenuOptions> queryWrapper = new LambdaQueryWrapper<>();

        // 根据菜单id查询
        Long menuId = sysMenuOptionsRequest.getMenuId();
        queryWrapper.eq(ObjectUtil.isNotNull(menuId), SysMenuOptions::getMenuId, menuId);

        // 根据查询文本查询
        String searchText = sysMenuOptionsRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.like(SysMenuOptions::getOptionCode, searchText);
            queryWrapper.or().like(SysMenuOptions::getOptionName, searchText);
        }

        return queryWrapper;
    }

}