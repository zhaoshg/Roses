package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.event.sdk.publish.BusinessEventPublisher;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.SysRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveUserCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserRole;
import cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.mapper.SysUserRoleMapper;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRoleRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserRoleService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.stylefeng.roses.kernel.sys.modular.user.constants.UserConstants.UPDATE_USER_ROLE_EVENT;

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

    @Resource(name = "userRoleCache")
    private CacheOperatorApi<List<Long>> userRoleCache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindRoles(SysUserRoleRequest sysUserRoleRequest) {

        // 不能修改超级管理员用户的角色，修改管理员角色可能登录会有问题
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

        // 发布修改用户绑定角色的事件
        BusinessEventPublisher.publishEvent(UPDATE_USER_ROLE_EVENT, sysUserRoleRequest.getUserId());

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

        // 发布修改用户绑定角色的事件
        BusinessEventPublisher.publishEvent(UPDATE_USER_ROLE_EVENT, userId);

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

        // 先从缓存查找用户的角色
        List<Long> cachedRoleIds = userRoleCache.get(userId.toString());
        if (cachedRoleIds != null) {
            return cachedRoleIds;
        }

        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        wrapper.select(SysUserRole::getRoleId);
        List<SysUserRole> sysUserRoleList = this.list(wrapper);

        List<Long> userRoleQueryResult = sysUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        // 查询结果缓存起来
        if (ObjectUtil.isNotEmpty(userRoleQueryResult)) {
            userRoleCache.put(userId.toString(), userRoleQueryResult, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
            return userRoleQueryResult;
        }

        return userRoleQueryResult;
    }

    @Override
    public List<Long> findUserIdsByRoleId(Long roleId) {
        SysUserRoleRequest userRoleRequest = new SysUserRoleRequest();
        userRoleRequest.setRoleId(roleId);
        LambdaQueryWrapper<SysUserRole> queryWrapper = this.createWrapper(userRoleRequest);
        queryWrapper.select(SysUserRole::getUserId);
        List<SysUserRole> list = this.list(queryWrapper);
        return list.stream().map(SysUserRole::getUserId).collect(Collectors.toList());
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

        Long roleId = sysUserRoleRequest.getRoleId();
        queryWrapper.eq(ObjectUtil.isNotNull(roleId), SysUserRole::getRoleId, roleId);

        return queryWrapper;
    }

}