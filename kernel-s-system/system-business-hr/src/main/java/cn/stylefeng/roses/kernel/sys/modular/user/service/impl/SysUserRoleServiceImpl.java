package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.SysRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveUserCallbackApi;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserRole;
import cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserRoleExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.mapper.SysUserRoleMapper;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRoleRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserRoleService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户角色关联业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService,
        RemoveUserCallbackApi, RemoveRoleCallbackApi {

    @Resource
    private SysRoleServiceApi sysRoleServiceApi;

    @Resource
    private SysUserService sysUserService;

    @Override
    public void add(SysUserRoleRequest sysUserRoleRequest) {
        SysUserRole sysUserRole = new SysUserRole();
        BeanUtil.copyProperties(sysUserRoleRequest, sysUserRole);
        this.save(sysUserRole);
    }

    @Override
    public void del(SysUserRoleRequest sysUserRoleRequest) {
        SysUserRole sysUserRole = this.querySysUserRole(sysUserRoleRequest);
        this.removeById(sysUserRole.getUserRoleId());
    }

    @Override
    public void edit(SysUserRoleRequest sysUserRoleRequest) {
        SysUserRole sysUserRole = this.querySysUserRole(sysUserRoleRequest);
        BeanUtil.copyProperties(sysUserRoleRequest, sysUserRole);
        this.updateById(sysUserRole);
    }

    @Override
    public SysUserRole detail(SysUserRoleRequest sysUserRoleRequest) {
        return this.querySysUserRole(sysUserRoleRequest);
    }

    @Override
    public PageResult<SysUserRole> findPage(SysUserRoleRequest sysUserRoleRequest) {
        LambdaQueryWrapper<SysUserRole> wrapper = createWrapper(sysUserRoleRequest);
        Page<SysUserRole> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindRoles(SysUserRoleRequest sysUserRoleRequest) {

        // 不能修改超级管理员用户的角色
        boolean userSuperAdminFlag = sysUserService.getUserSuperAdminFlag(sysUserRoleRequest.getUserId());
        if (userSuperAdminFlag) {
            throw new ServiceException(SysUserExceptionEnum.CANT_CHANGE_ADMIN_ROLE);
        }

        // 清空已有的用户角色绑定
        LambdaQueryWrapper<SysUserRole> wrapper = this.createWrapper(sysUserRoleRequest);
        this.remove(wrapper);

        // 重新绑定用户角色信息
        Set<Long> roleIdList = sysUserRoleRequest.getRoleIdList();
        ArrayList<SysUserRole> newUserRoles = new ArrayList<>();
        for (Long newRoleId : roleIdList) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUserRoleRequest.getUserId());
            sysUserRole.setRoleId(newRoleId);
            newUserRoles.add(sysUserRole);
        }
        this.saveBatch(newUserRoles);
    }

    @Override
    public void bindUserDefaultRole(Long userId) {

        // 查询默认角色的角色id
        Long defaultRoleId = sysRoleServiceApi.getDefaultRoleId();

        // 给用户绑定默认角色
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userId);
        sysUserRole.setRoleId(defaultRoleId);
        this.save(sysUserRole);
    }

    @Override
    public List<SysUserRole> findList(SysUserRoleRequest sysUserRoleRequest) {
        LambdaQueryWrapper<SysUserRole> wrapper = this.createWrapper(sysUserRoleRequest);
        return this.list(wrapper);
    }

    @Override
    public void validateHaveUserBind(Set<Long> beRemovedUserIdList) {
        // none
    }

    @Override
    public void removeUserAction(Set<Long> beRemovedUserIdList) {
        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserRoleLambdaQueryWrapper.in(SysUserRole::getUserId, beRemovedUserIdList);
        this.remove(sysUserRoleLambdaQueryWrapper);
    }

    @Override
    public void validateHaveRoleBind(Set<Long> beRemovedRoleIdList) {
        // none
    }

    @Override
    public void removeRoleAction(Set<Long> beRemovedRoleIdList) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUserRole::getRoleId, beRemovedRoleIdList);
        this.remove(wrapper);
    }

    @Override
    public List<Long> getUserRoleIdList(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        wrapper.select(SysUserRole::getRoleId);
        List<SysUserRole> sysUserRoleList = this.list(wrapper);

        return sysUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    private SysUserRole querySysUserRole(SysUserRoleRequest sysUserRoleRequest) {
        SysUserRole sysUserRole = this.getById(sysUserRoleRequest.getUserRoleId());
        if (ObjectUtil.isEmpty(sysUserRole)) {
            throw new ServiceException(SysUserRoleExceptionEnum.SYS_USER_ROLE_NOT_EXISTED);
        }
        return sysUserRole;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    private LambdaQueryWrapper<SysUserRole> createWrapper(SysUserRoleRequest sysUserRoleRequest) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();

        Long userId = sysUserRoleRequest.getUserId();
        queryWrapper.eq(ObjectUtil.isNotNull(userId), SysUserRole::getUserId, userId);

        return queryWrapper;
    }

}