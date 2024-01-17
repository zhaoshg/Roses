package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.event.sdk.publish.BusinessEventPublisher;
import cn.stylefeng.roses.kernel.log.api.util.BusinessLogUtil;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.SysRoleLimitServiceApi;
import cn.stylefeng.roses.kernel.sys.api.SysRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveUserCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.api.enums.role.RoleTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.SysRoleDTO;
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
import java.util.HashSet;
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
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService, RemoveUserCallbackApi, RemoveRoleCallbackApi {

    @Resource
    private SysRoleServiceApi sysRoleServiceApi;

    @Resource
    private SysUserService sysUserService;

    @Resource(name = "userRoleCache")
    private CacheOperatorApi<List<SysUserRole>> userRoleCache;

    @Resource
    private SysRoleLimitServiceApi sysRoleLimitServiceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindRoles(SysUserRoleRequest sysUserRoleRequest) {

        // 不能修改超级管理员用户的角色，修改管理员角色可能登录会有问题
        boolean userSuperAdminFlag = sysUserService.getUserSuperAdminFlag(sysUserRoleRequest.getUserId());
        if (userSuperAdminFlag) {
            throw new ServiceException(SysUserExceptionEnum.CANT_CHANGE_ADMIN_ROLE);
        }

        // 清空已有的用户角色绑定
        this.removeRoleAlreadyBind(sysUserRoleRequest);

        // 获取角色的详情
        Set<Long> roleIdList = sysUserRoleRequest.getRoleIdList();
        List<SysRoleDTO> sysRoleDTOList = sysRoleServiceApi.getRolesByIds(new ArrayList<>(roleIdList));

        // 重新绑定用户角色信息
        List<SysUserRole> newUserRoles = this.createUserRoleBinds(sysUserRoleRequest, roleIdList, sysRoleDTOList);
        this.saveBatch(newUserRoles);

        // 发布修改用户绑定角色的事件
        BusinessEventPublisher.publishEvent(UPDATE_USER_ROLE_EVENT, sysUserRoleRequest.getUserId());

        // 记录日志
        BusinessLogUtil.setLogTitle("为用户绑定角色，用户账号：", sysUserService.getUserRealName(sysUserRoleRequest.getUserId()));
        BusinessLogUtil.addContent("用户id：\n", sysUserRoleRequest.getUserId());
        BusinessLogUtil.addContent("用户角色信息如下：\n", roleIdList);
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
        List<SysUserRole> cachedRoleList = userRoleCache.get(userId.toString());
        if (ObjectUtil.isNotEmpty(cachedRoleList)) {
            return cachedRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        }

        List<SysUserRole> sysUserRoleList = this.dbGetUserTotalRoleList(userId);

        // 查询结果缓存起来
        if (ObjectUtil.isNotEmpty(sysUserRoleList)) {
            userRoleCache.put(userId.toString(), sysUserRoleList, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
        }

        return sysUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getUserRoleIdListCurrentCompany(Long userId, Long companyId) {

        // 先从缓存查找用户的角色
        List<SysUserRole> cachedRoleList = userRoleCache.get(userId.toString());
        if (ObjectUtil.isNotEmpty(cachedRoleList)) {
            return this.getUserCompanyPermissionRole(cachedRoleList, companyId);
        }

        List<SysUserRole> sysUserRoleList = this.dbGetUserTotalRoleList(userId);

        // 查询结果缓存起来
        if (ObjectUtil.isNotEmpty(sysUserRoleList)) {
            userRoleCache.put(userId.toString(), sysUserRoleList, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
        }

        return this.getUserCompanyPermissionRole(sysUserRoleList, companyId);
    }

    @Override
    public List<Long> findUserIdsByRoleId(Long roleId) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getRoleId, roleId);
        queryWrapper.select(SysUserRole::getUserId);
        List<SysUserRole> list = this.list(queryWrapper);
        return list.stream().map(SysUserRole::getUserId).collect(Collectors.toList());
    }

    @Override
    public Set<Long> findUserRoleLimitScope(Long userId) {

        // 获取用户的所有角色id
        List<Long> userRoleIdList = this.getUserRoleIdList(userId);

        // 获取角色的限制范围列表（菜单和菜单功能id集合）
        return this.sysRoleLimitServiceApi.getRoleBindLimitList(userRoleIdList);
    }

    @Override
    public Set<Long> findCurrentUserRoleLimitScope() {

        Long userId = LoginContext.me().getLoginUser().getUserId();

        return this.findUserRoleLimitScope(userId);
    }

    /**
     * 清空用户角色的绑定
     *
     * @author fengshuonan
     * @since 2024/1/17 0:24
     */
    private void removeRoleAlreadyBind(SysUserRoleRequest sysUserRoleRequest) {

        // 1. 获取系统中的所有角色，包括系统角色和当前公司角色
        List<SysRoleDTO> roleResults = sysRoleServiceApi.getSystemRoleAndCurrentCompanyRole(LoginContext.me().getCurrentUserCompanyId());
        if (ObjectUtil.isEmpty(roleResults)) {
            return;
        }

        // 2. 所有系统角色id集合
        Set<Long> systemRoleIdList = roleResults.stream().filter(i -> i.getRoleType().equals(RoleTypeEnum.SYSTEM_ROLE.getCode())).map(SysRoleDTO::getRoleId).collect(Collectors.toSet());

        // 3. 所有当前公司角色id集合
        Set<Long> currentCompanyRoleIdList = roleResults.stream().filter(i -> i.getRoleType().equals(RoleTypeEnum.COMPANY_ROLE.getCode())).map(SysRoleDTO::getRoleId).collect(Collectors.toSet());

        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();

        // 2. 当前如果是超级管理员在操作，清空用户的系统角色和当前公司角色（因为超级管理员的绑定角色列表，可以看到系统角色和当前公司角色）
        boolean superAdminFlag = LoginContext.me().getSuperAdminFlag();
        if (superAdminFlag) {

            Set<Long> paramsRoleIdList = new HashSet<>(systemRoleIdList);
            if (ObjectUtil.isNotEmpty(currentCompanyRoleIdList)) {
                paramsRoleIdList.addAll(currentCompanyRoleIdList);
            }

            queryWrapper.eq(SysUserRole::getUserId, sysUserRoleRequest.getUserId());
            queryWrapper.in(SysUserRole::getRoleId, paramsRoleIdList);
            this.remove(queryWrapper);
        }

        // 3. 如果当前不是超级管理员，则只能清空当前公司的角色id集合
        else {
            if (ObjectUtil.isEmpty(currentCompanyRoleIdList)) {
                return;
            }
            queryWrapper.eq(SysUserRole::getUserId, sysUserRoleRequest.getUserId());
            queryWrapper.in(SysUserRole::getRoleId, currentCompanyRoleIdList);
            this.remove(queryWrapper);
        }
    }

    /**
     * 从数据库获取所有的用户角色
     *
     * @author fengshuonan
     * @since 2024-01-17 16:40
     */
    private List<SysUserRole> dbGetUserTotalRoleList(Long userId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        wrapper.select(SysUserRole::getRoleId, SysUserRole::getRoleCompanyId, SysUserRole::getRoleType);
        return this.list(wrapper);
    }

    /**
     * 获取当前用户登录公司的角色id集合
     * <p>
     * 从参数中找到当前用户登录公司的 + 用户基本的角色集合
     *
     * @author fengshuonan
     * @since 2024-01-17 16:16
     */
    private List<Long> getUserCompanyPermissionRole(List<SysUserRole> paramRoles, Long userCurrentCompanyId) {

        // 1. 先获取最基本的用户角色，不分公司的，每个人都有的角色
        Set<Long> baseRoleIdList = paramRoles.stream().filter(i -> RoleTypeEnum.SYSTEM_ROLE.getCode().equals(i.getRoleType()) && i.getRoleCompanyId() == null).map(SysUserRole::getRoleId)
                .collect(Collectors.toSet());

        // 没传当前公司id，则只返回最基本的角色
        if (ObjectUtil.isEmpty(userCurrentCompanyId)) {
            return new ArrayList<>(baseRoleIdList);
        }

        // 2. 获取用户当前登录公司下的角色id集合
        Set<Long> currentCompanyRoleIdList = paramRoles.stream()
                .filter(i -> RoleTypeEnum.COMPANY_ROLE.getCode().equals(i.getRoleType())
                        && i.getRoleCompanyId() != null
                        && i.getRoleCompanyId().equals(userCurrentCompanyId))
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toSet());

        // 3. 合并两个集合并返回
        List<Long> resultRoleIdList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(baseRoleIdList)) {
            resultRoleIdList.addAll(baseRoleIdList);
        }
        if (ObjectUtil.isNotEmpty(currentCompanyRoleIdList)) {
            resultRoleIdList.addAll(currentCompanyRoleIdList);
        }
        return resultRoleIdList;
    }

    /**
     * 创建用户角色的绑定
     *
     * @param sysUserRoleRequest 接口请求参数
     * @param roleIdList         用户绑定的角色列表
     * @param sysRoleDTOList     用来获取角色的公司id和角色类型，补充信息用
     * @author fengshuonan
     * @since 2024-01-17 17:27
     */
    private List<SysUserRole> createUserRoleBinds(SysUserRoleRequest sysUserRoleRequest, Set<Long> roleIdList, List<SysRoleDTO> sysRoleDTOList) {
        List<SysUserRole> newUserRoles = new ArrayList<>();
        for (Long newRoleId : roleIdList) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUserRoleRequest.getUserId());
            sysUserRole.setRoleId(newRoleId);

            // 填充角色的类型和所属公司
            for (SysRoleDTO sysRoleDTO : sysRoleDTOList) {
                if (sysRoleDTO.getRoleId().equals(newRoleId)) {
                    sysUserRole.setRoleType(sysRoleDTO.getRoleType());
                    sysUserRole.setRoleCompanyId(sysRoleDTO.getRoleCompanyId());
                    break;
                }
            }

            newUserRoles.add(sysUserRole);
        }
        return newUserRoles;
    }

}