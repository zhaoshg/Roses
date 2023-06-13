package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.exception.SysRoleMenuExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleMenuMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleMenuRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 角色菜单关联业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService, RemoveRoleCallbackApi {

    @Override
    public void add(SysRoleMenuRequest sysRoleMenuRequest) {
        SysRoleMenu sysRoleMenu = new SysRoleMenu();
        BeanUtil.copyProperties(sysRoleMenuRequest, sysRoleMenu);
        this.save(sysRoleMenu);
    }

    @Override
    public void del(SysRoleMenuRequest sysRoleMenuRequest) {
        SysRoleMenu sysRoleMenu = this.querySysRoleMenu(sysRoleMenuRequest);
        this.removeById(sysRoleMenu.getRoleMenuId());
    }

    @Override
    public void edit(SysRoleMenuRequest sysRoleMenuRequest) {
        SysRoleMenu sysRoleMenu = this.querySysRoleMenu(sysRoleMenuRequest);
        BeanUtil.copyProperties(sysRoleMenuRequest, sysRoleMenu);
        this.updateById(sysRoleMenu);
    }

    @Override
    public SysRoleMenu detail(SysRoleMenuRequest sysRoleMenuRequest) {
        return this.querySysRoleMenu(sysRoleMenuRequest);
    }

    @Override
    public PageResult<SysRoleMenu> findPage(SysRoleMenuRequest sysRoleMenuRequest) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = createWrapper(sysRoleMenuRequest);
        Page<SysRoleMenu> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysRoleMenu> findList(SysRoleMenuRequest sysRoleMenuRequest) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = this.createWrapper(sysRoleMenuRequest);
        return this.list(wrapper);
    }

    @Override
    public void validateHaveRoleBind(Set<Long> beRemovedRoleIdList) {
        // none
    }

    @Override
    public void removeRoleAction(Set<Long> beRemovedRoleIdList) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysRoleMenu::getRoleId, beRemovedRoleIdList);
        this.remove(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private SysRoleMenu querySysRoleMenu(SysRoleMenuRequest sysRoleMenuRequest) {
        SysRoleMenu sysRoleMenu = this.getById(sysRoleMenuRequest.getRoleMenuId());
        if (ObjectUtil.isEmpty(sysRoleMenu)) {
            throw new ServiceException(SysRoleMenuExceptionEnum.SYS_ROLE_MENU_NOT_EXISTED);
        }
        return sysRoleMenu;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private LambdaQueryWrapper<SysRoleMenu> createWrapper(SysRoleMenuRequest sysRoleMenuRequest) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();

        Long roleMenuId = sysRoleMenuRequest.getRoleMenuId();
        Long roleId = sysRoleMenuRequest.getRoleId();
        Long menuId = sysRoleMenuRequest.getMenuId();

        queryWrapper.eq(ObjectUtil.isNotNull(roleMenuId), SysRoleMenu::getRoleMenuId, roleMenuId);
        queryWrapper.eq(ObjectUtil.isNotNull(roleId), SysRoleMenu::getRoleId, roleId);
        queryWrapper.eq(ObjectUtil.isNotNull(menuId), SysRoleMenu::getMenuId, menuId);

        return queryWrapper;
    }

}