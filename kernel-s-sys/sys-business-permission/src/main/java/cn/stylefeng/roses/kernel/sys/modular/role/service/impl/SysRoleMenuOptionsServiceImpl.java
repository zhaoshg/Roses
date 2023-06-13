package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.modular.role.action.RoleAssignOperateAction;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.PermissionNodeTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.exception.SysRoleMenuOptionsExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleMenuOptionsMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindPermissionRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleMenuOptionsRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindPermissionItem;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 角色和菜单下的功能关联业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Service
public class SysRoleMenuOptionsServiceImpl extends ServiceImpl<SysRoleMenuOptionsMapper, SysRoleMenuOptions> implements
        SysRoleMenuOptionsService, RemoveRoleCallbackApi, RoleAssignOperateAction {

    @Override
    public void add(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        SysRoleMenuOptions sysRoleMenuOptions = new SysRoleMenuOptions();
        BeanUtil.copyProperties(sysRoleMenuOptionsRequest, sysRoleMenuOptions);
        this.save(sysRoleMenuOptions);
    }

    @Override
    public void del(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        SysRoleMenuOptions sysRoleMenuOptions = this.querySysRoleMenuOptions(sysRoleMenuOptionsRequest);
        this.removeById(sysRoleMenuOptions.getRoleMenuOptionId());
    }

    @Override
    public void edit(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        SysRoleMenuOptions sysRoleMenuOptions = this.querySysRoleMenuOptions(sysRoleMenuOptionsRequest);
        BeanUtil.copyProperties(sysRoleMenuOptionsRequest, sysRoleMenuOptions);
        this.updateById(sysRoleMenuOptions);
    }

    @Override
    public SysRoleMenuOptions detail(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        return this.querySysRoleMenuOptions(sysRoleMenuOptionsRequest);
    }

    @Override
    public PageResult<SysRoleMenuOptions> findPage(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        LambdaQueryWrapper<SysRoleMenuOptions> wrapper = createWrapper(sysRoleMenuOptionsRequest);
        Page<SysRoleMenuOptions> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysRoleMenuOptions> findList(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        LambdaQueryWrapper<SysRoleMenuOptions> wrapper = this.createWrapper(sysRoleMenuOptionsRequest);
        return this.list(wrapper);
    }

    @Override
    public void validateHaveRoleBind(Set<Long> beRemovedRoleIdList) {
        // none
    }

    @Override
    public void removeRoleAction(Set<Long> beRemovedRoleIdList) {
        LambdaQueryWrapper<SysRoleMenuOptions> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysRoleMenuOptions::getRoleId, beRemovedRoleIdList);
        this.remove(wrapper);
    }

    @Override
    public PermissionNodeTypeEnum getNodeType() {
        return PermissionNodeTypeEnum.OPTIONS;
    }

    @Override
    public List<RoleBindPermissionItem> doOperateAction(RoleBindPermissionRequest roleBindPermissionRequest) {

        Long roleId = roleBindPermissionRequest.getRoleId();
        Long menuOptionId = roleBindPermissionRequest.getNodeId();

        if (roleBindPermissionRequest.getChecked()) {
            SysRoleMenuOptions sysRoleMenuOptions = new SysRoleMenuOptions();
            sysRoleMenuOptions.setRoleId(roleId);
            sysRoleMenuOptions.setMenuOptionId(menuOptionId);
            this.save(sysRoleMenuOptions);
        } else {
            LambdaUpdateWrapper<SysRoleMenuOptions> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(SysRoleMenuOptions::getRoleId, roleId);
            wrapper.eq(SysRoleMenuOptions::getMenuOptionId, menuOptionId);
            this.remove(wrapper);
        }

        return null;
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private SysRoleMenuOptions querySysRoleMenuOptions(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        SysRoleMenuOptions sysRoleMenuOptions = this.getById(sysRoleMenuOptionsRequest.getRoleMenuOptionId());
        if (ObjectUtil.isEmpty(sysRoleMenuOptions)) {
            throw new ServiceException(SysRoleMenuOptionsExceptionEnum.SYS_ROLE_MENU_OPTIONS_NOT_EXISTED);
        }
        return sysRoleMenuOptions;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private LambdaQueryWrapper<SysRoleMenuOptions> createWrapper(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        LambdaQueryWrapper<SysRoleMenuOptions> queryWrapper = new LambdaQueryWrapper<>();

        Long roleId = sysRoleMenuOptionsRequest.getRoleId();
        queryWrapper.eq(ObjectUtil.isNotNull(roleId), SysRoleMenuOptions::getRoleId, roleId);

        return queryWrapper;
    }

}