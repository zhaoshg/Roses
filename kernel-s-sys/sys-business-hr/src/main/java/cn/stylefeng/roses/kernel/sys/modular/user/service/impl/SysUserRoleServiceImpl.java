package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveUserCallbackApi;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserRole;
import cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserRoleExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.mapper.SysUserRoleMapper;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRoleRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 用户角色关联业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService, RemoveUserCallbackApi {

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
    public List<SysUserRole> findList(SysUserRoleRequest sysUserRoleRequest) {
        LambdaQueryWrapper<SysUserRole> wrapper = this.createWrapper(sysUserRoleRequest);
        return this.list(wrapper);
    }

    @Override
    public void validateHaveUserBind(Set<Long> beRemovedUserIdList) {

    }

    @Override
    public void removeUserAction(Set<Long> beRemovedUserIdList) {
        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserRoleLambdaQueryWrapper.in(SysUserRole::getUserId, beRemovedUserIdList);
        this.remove(sysUserRoleLambdaQueryWrapper);
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

        Long userRoleId = sysUserRoleRequest.getUserRoleId();
        Long userId = sysUserRoleRequest.getUserId();
        Long roleId = sysUserRoleRequest.getRoleId();
        Long tenantId = sysUserRoleRequest.getTenantId();

        queryWrapper.eq(ObjectUtil.isNotNull(userRoleId), SysUserRole::getUserRoleId, userRoleId);
        queryWrapper.eq(ObjectUtil.isNotNull(userId), SysUserRole::getUserId, userId);
        queryWrapper.eq(ObjectUtil.isNotNull(roleId), SysUserRole::getRoleId, roleId);
        queryWrapper.eq(ObjectUtil.isNotNull(tenantId), SysUserRole::getTenantId, tenantId);

        return queryWrapper;
    }

}