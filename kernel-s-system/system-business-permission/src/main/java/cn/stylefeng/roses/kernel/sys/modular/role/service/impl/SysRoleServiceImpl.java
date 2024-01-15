package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.util.BusinessLogUtil;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.SysUserRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.api.enums.permission.DataScopeTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.enums.role.RoleTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.role.SysRoleDTO;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRole;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.exception.SysRoleExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统角色业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysUserRoleServiceApi sysUserRoleServiceApi;

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    @Resource
    private SysMenuOptionsService sysMenuOptionsService;

    @Override
    public void add(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = new SysRole();
        BeanUtil.copyProperties(sysRoleRequest, sysRole);

        // 设置角色默认的数据范围，默认查看全部
        sysRole.setDataScopeType(DataScopeTypeEnum.ALL.getCode());

        this.save(sysRole);

        // 添加日志
        BusinessLogUtil.setLogTitle("添加角色，角色名称：", sysRoleRequest.getRoleName());
        BusinessLogUtil.addContent("角色信息详情如下：\n", sysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);

        // 系统角色不能被删除
        if (RoleTypeEnum.SYSTEM_ROLE.getCode().equals(sysRole.getRoleType())) {
            throw new ServiceException(SysRoleExceptionEnum.SYSTEM_ROLE_CANT_DELETE);
        }

        // 非管理员，只能删除自己的角色
        if (!LoginContext.me().getSuperAdminFlag()) {
            if (!sysRole.getCreateUser().equals(LoginContext.me().getLoginUser().getUserId())) {
                throw new ServiceException(SysRoleExceptionEnum.DEL_PERMISSION_ERROR);
            }
        }

        // 删除角色
        this.baseDelete(CollectionUtil.set(false, sysRole.getRoleId()));

        // 添加日志
        BusinessLogUtil.setLogTitle("删除角色，角色名称：", sysRole.getRoleName());
        BusinessLogUtil.addContent("角色信息详情如下：\n", sysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(SysRoleRequest sysRoleRequest) {

        // 校验被删除的角色中是否有管理员角色
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRole::getRoleId, sysRoleRequest.getRoleIdList());
        queryWrapper.eq(SysRole::getRoleType, RoleTypeEnum.SYSTEM_ROLE.getCode());
        long haveSystemFlagCount = this.count(queryWrapper);
        if (haveSystemFlagCount > 0) {
            throw new ServiceException(SysRoleExceptionEnum.SYSTEM_ROLE_CANT_DELETE);
        }

        // 如果当前用户是非管理员，则只能删除自己创建的角色
        if (!LoginContext.me().getSuperAdminFlag()) {
            LambdaQueryWrapper<SysRole> tempWrapper = new LambdaQueryWrapper<>();
            tempWrapper.in(SysRole::getRoleId, sysRoleRequest.getRoleIdList());
            tempWrapper.ne(BaseEntity::getCreateUser, LoginContext.me().getLoginUser().getUserId());
            long notMeCreateCount = this.count(tempWrapper);
            if (notMeCreateCount > 0) {
                throw new ServiceException(SysRoleExceptionEnum.DEL_PERMISSION_ERROR);
            }
        }

        // 执行删除角色
        this.baseDelete(sysRoleRequest.getRoleIdList());

        // 添加日志
        BusinessLogUtil.setLogTitle("批量删除角色");
        BusinessLogUtil.addContent("角色id集合如下：\n", sysRoleRequest.getRoleIdList());
    }

    @Override
    public void edit(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);

        // 添加日志
        BusinessLogUtil.setLogTitle("修改角色信息");
        BusinessLogUtil.addContent("原角色信息如下：\n", sysRole);

        // 不允许修改角色编码
        if (!sysRole.getRoleCode().equals(sysRoleRequest.getRoleCode())) {
            throw new ServiceException(SysRoleExceptionEnum.SUPER_ADMIN_ROLE_CODE_ERROR);
        }

        BeanUtil.copyProperties(sysRoleRequest, sysRole);
        this.updateById(sysRole);

        BusinessLogUtil.addContent("修改后角色信息如下：\n", sysRole);
    }

    @Override
    public SysRole detail(SysRoleRequest sysRoleRequest) {
        return this.querySysRole(sysRoleRequest);
    }

    @Override
    public PageResult<SysRole> findPage(SysRoleRequest sysRoleRequest) {
        LambdaQueryWrapper<SysRole> wrapper = createWrapper(sysRoleRequest);

        // 只查询需要的字段
        wrapper.select(SysRole::getRoleName, SysRole::getRoleCode, SysRole::getRoleSort, SysRole::getRoleId, BaseEntity::getCreateTime);

        // 非管理员用户只能查看自己创建的角色
        this.filterRolePermission(wrapper);

        Page<SysRole> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public Integer getRoleDataScopeType(Long roleId) {

        // 角色id为空，返回仅本人数据
        if (ObjectUtil.isEmpty(roleId)) {
            return DataScopeTypeEnum.SELF.getCode();
        }

        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getRoleId, roleId);
        sysRoleLambdaQueryWrapper.select(SysRole::getDataScopeType);
        SysRole sysRole = this.getOne(sysRoleLambdaQueryWrapper, false);

        if (sysRole != null) {
            Integer dataScopeType = sysRole.getDataScopeType();
            if (dataScopeType != null) {
                return dataScopeType;
            }
        }

        return DataScopeTypeEnum.SELF.getCode();
    }

    @Override
    public void updateRoleDataScopeType(Long roleId, Integer dataScopeType) {

        if (ObjectUtil.isEmpty(roleId) || ObjectUtil.isEmpty(dataScopeType)) {
            return;
        }

        LambdaUpdateWrapper<SysRole> sysRoleLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        sysRoleLambdaUpdateWrapper.eq(SysRole::getRoleId, roleId);
        sysRoleLambdaUpdateWrapper.set(SysRole::getDataScopeType, dataScopeType);
        this.update(sysRoleLambdaUpdateWrapper);
    }

    @Override
    public DataScopeTypeEnum getRoleDataScope(List<Long> roleIds) {

        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.in(SysRole::getRoleId, roleIds);
        sysRoleLambdaQueryWrapper.select(SysRole::getDataScopeType);

        // 按数据范围降序排列，50-全部数据最大
        sysRoleLambdaQueryWrapper.orderByDesc(SysRole::getDataScopeType);

        SysRole sysRole = this.getOne(sysRoleLambdaQueryWrapper, false);
        if (sysRole != null) {
            if (sysRole.getDataScopeType() != null) {
                return DataScopeTypeEnum.codeToEnum(sysRole.getDataScopeType());
            }
        }

        // 如果是查询不到，则直接返回仅本人数据
        return DataScopeTypeEnum.SELF;
    }

    @Override
    public List<SysRole> findList(SysRoleRequest sysRoleRequest) {
        LambdaQueryWrapper<SysRole> wrapper = this.createWrapper(sysRoleRequest);

        // 只查询id和名称
        wrapper.select(SysRole::getRoleId, SysRole::getRoleName);

        // 填写角色的权限信息
        this.filterRolePermission(wrapper);

        return this.list(wrapper);
    }

    @Override
    public Long getDefaultRoleId() {

        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getRoleCode, SysConstants.DEFAULT_ROLE_CODE);
        sysRoleLambdaQueryWrapper.select(SysRole::getRoleId);
        SysRole sysRole = this.getOne(sysRoleLambdaQueryWrapper, false);

        if (sysRole != null) {
            return sysRole.getRoleId();
        }

        return null;
    }

    @Override
    public String getRoleNameByRoleId(Long roleId) {

        if (ObjectUtil.isEmpty(roleId)) {
            return "";
        }

        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getRoleId, roleId);
        sysRoleLambdaQueryWrapper.select(SysRole::getRoleName);
        SysRole sysRole = this.getOne(sysRoleLambdaQueryWrapper, false);
        if (sysRole != null) {
            return sysRole.getRoleName();
        }

        return "";
    }

    @Override
    public List<String> getRoleMenuOptionsByRoleId(String roleCode) {

        if (ObjectUtil.isEmpty(roleCode)) {
            return new ArrayList<>();
        }

        // 获取角色编码对应的角色id
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getRoleCode, roleCode);
        sysRoleLambdaQueryWrapper.select(SysRole::getRoleId);
        SysRole sysRole = this.getOne(sysRoleLambdaQueryWrapper, false);
        if (sysRole == null) {
            return new ArrayList<>();
        }
        Long roleId = sysRole.getRoleId();

        // 获取角色的角色功能id集合
        List<Long> roleBindMenuOptionsIdList = sysRoleMenuOptionsService.getRoleBindMenuOptionsIdList(ListUtil.list(false, roleId));
        if (ObjectUtil.isEmpty(roleBindMenuOptionsIdList)) {
            return new ArrayList<>();
        }

        // 获取角色功能id的集合对应的功能编码集合
        return sysMenuOptionsService.getOptionsCodeList(roleBindMenuOptionsIdList);
    }

    @Override
    public List<SysRoleDTO> getRolesByIds(List<Long> roleIds) {

        if (ObjectUtil.isEmpty(roleIds)) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.in(SysRole::getRoleId, roleIds);
        sysRoleLambdaQueryWrapper.select(SysRole::getRoleName, SysRole::getRoleId, SysRole::getRoleCode);
        List<SysRole> sysRoleList = this.list(sysRoleLambdaQueryWrapper);

        if (ObjectUtil.isEmpty(sysRoleList)) {
            return new ArrayList<>();
        }

        return BeanUtil.copyToList(sysRoleList, SysRoleDTO.class, CopyOptions.create().ignoreError());
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private SysRole querySysRole(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.getById(sysRoleRequest.getRoleId());
        if (ObjectUtil.isEmpty(sysRole)) {
            throw new ServiceException(SysRoleExceptionEnum.SYS_ROLE_NOT_EXISTED);
        }
        return sysRole;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private LambdaQueryWrapper<SysRole> createWrapper(SysRoleRequest sysRoleRequest) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();

        // 根据名称模糊搜索
        String searchText = sysRoleRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.like(SysRole::getRoleName, searchText);
            queryWrapper.or().like(SysRole::getRoleCode, searchText);
        }

        // 排序字段
        queryWrapper.orderByAsc(SysRole::getRoleSort);

        return queryWrapper;
    }

    /**
     * 删除角色的基础业务操作
     *
     * @author fengshuonan
     * @since 2023/6/12 21:14
     */
    private void baseDelete(Set<Long> roleIdList) {
        // 执行角色相关的校验
        Map<String, RemoveRoleCallbackApi> callbackApiMap = SpringUtil.getBeansOfType(RemoveRoleCallbackApi.class);
        for (RemoveRoleCallbackApi removeRoleCallbackApi : callbackApiMap.values()) {
            removeRoleCallbackApi.validateHaveRoleBind(roleIdList);
        }

        // 执行角色相关关联业务的删除操作
        for (RemoveRoleCallbackApi removeRoleCallbackApi : callbackApiMap.values()) {
            removeRoleCallbackApi.removeRoleAction(roleIdList);
        }

        // 删除角色
        this.removeBatchByIds(roleIdList);
    }

    /**
     * 过滤角色的权限展示
     * <p>
     * 非管理员只能看到自己的角色和自己创建的角色
     *
     * @author fengshuonan
     * @since 2023/10/9 10:44
     */
    private void filterRolePermission(LambdaQueryWrapper<SysRole> wrapper) {
        if (!LoginContext.me().getSuperAdminFlag()) {
            Long userId = LoginContext.me().getLoginUser().getUserId();
            wrapper.eq(SysRole::getCreateUser, userId).or().in(SysRole::getRoleId, sysUserRoleServiceApi.getUserRoleIdList(userId));
        }
    }

}