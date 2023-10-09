package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.util.BusinessLogUtil;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.enums.permission.DataScopeTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleDataScope;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.exception.SysRoleDataScopeExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleDataScopeMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.RoleBindDataScopeRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleDataScopeRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.response.RoleBindDataScopeResponse;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleDataScopeService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色数据范围业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Service
public class SysRoleDataScopeServiceImpl extends ServiceImpl<SysRoleDataScopeMapper, SysRoleDataScope> implements SysRoleDataScopeService,
        RemoveRoleCallbackApi {

    @Resource
    private SysRoleService sysRoleService;

    @Override
    public void add(SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        SysRoleDataScope sysRoleDataScope = new SysRoleDataScope();
        BeanUtil.copyProperties(sysRoleDataScopeRequest, sysRoleDataScope);
        this.save(sysRoleDataScope);
    }

    @Override
    public void del(SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        SysRoleDataScope sysRoleDataScope = this.querySysRoleDataScope(sysRoleDataScopeRequest);
        this.removeById(sysRoleDataScope.getRoleDataScopeId());
    }

    @Override
    public void edit(SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        SysRoleDataScope sysRoleDataScope = this.querySysRoleDataScope(sysRoleDataScopeRequest);
        BeanUtil.copyProperties(sysRoleDataScopeRequest, sysRoleDataScope);
        this.updateById(sysRoleDataScope);
    }

    @Override
    public SysRoleDataScope detail(SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        return this.querySysRoleDataScope(sysRoleDataScopeRequest);
    }

    @Override
    public PageResult<SysRoleDataScope> findPage(SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        LambdaQueryWrapper<SysRoleDataScope> wrapper = createWrapper(sysRoleDataScopeRequest);
        Page<SysRoleDataScope> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysRoleDataScope> findList(SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        LambdaQueryWrapper<SysRoleDataScope> wrapper = this.createWrapper(sysRoleDataScopeRequest);
        return this.list(wrapper);
    }

    @Override
    public RoleBindDataScopeResponse getRoleBindDataScope(RoleBindDataScopeRequest roleBindDataScopeRequest) {

        RoleBindDataScopeResponse roleBindDataScopeResponse = new RoleBindDataScopeResponse();
        roleBindDataScopeResponse.setOrgIdList(new ArrayList<>());

        // 获取角色的数据范围类型
        Integer dataScopeType = sysRoleService.getRoleDataScopeType(roleBindDataScopeRequest.getRoleId());
        roleBindDataScopeResponse.setDataScopeType(dataScopeType);

        if (!DataScopeTypeEnum.DEFINE.getCode().equals(dataScopeType)) {
            return roleBindDataScopeResponse;
        }

        // 如果是指定部门，则获取指定部门的orgId集合
        Set<Long> roleBindOrgIdList = this.getRoleBindOrgIdList(ListUtil.list(false, roleBindDataScopeRequest.getRoleId()));
        roleBindDataScopeResponse.setOrgIdList(CollectionUtil.list(false, roleBindOrgIdList));

        return roleBindDataScopeResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleBindDataScope(RoleBindDataScopeRequest roleBindDataScopeRequest) {

        BusinessLogUtil.setLogTitle("修改角色数据范围");
        BusinessLogUtil.addContent("角色绑定数据范围的参数如下：\n", roleBindDataScopeRequest);

        // 无论角色是否绑定的是指定部门，清空掉角色绑定的数据范围
        LambdaQueryWrapper<SysRoleDataScope> removeWrapper = new LambdaQueryWrapper<>();
        removeWrapper.eq(SysRoleDataScope::getRoleId, roleBindDataScopeRequest.getRoleId());
        this.remove(removeWrapper);

        // 更新角色的数据权限类型
        Integer dataScopeType = roleBindDataScopeRequest.getDataScopeType();
        this.sysRoleService.updateRoleDataScopeType(roleBindDataScopeRequest.getRoleId(), dataScopeType);

        // 非指定部门，直接返回
        if (!DataScopeTypeEnum.DEFINE.getCode().equals(dataScopeType)) {
            return;
        }

        // 如果是指定部门的话，则更新角色关联的指定部门的信息
        List<Long> orgIdList = roleBindDataScopeRequest.getOrgIdList();
        if (ObjectUtil.isEmpty(orgIdList)) {
            return;
        }

        ArrayList<SysRoleDataScope> bindRoleDataScopeList = new ArrayList<>();
        for (Long orgId : orgIdList) {
            SysRoleDataScope sysRoleDataScope = new SysRoleDataScope();
            sysRoleDataScope.setRoleId(roleBindDataScopeRequest.getRoleId());
            sysRoleDataScope.setOrganizationId(orgId);
            bindRoleDataScopeList.add(sysRoleDataScope);
        }
        this.saveBatch(bindRoleDataScopeList);
    }

    @Override
    public Set<Long> getRoleBindOrgIdList(List<Long> roleIdList) {

        if (ObjectUtil.isEmpty(roleIdList)) {
            return new HashSet<>();
        }

        LambdaQueryWrapper<SysRoleDataScope> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysRoleDataScope::getRoleId, roleIdList);
        wrapper.select(SysRoleDataScope::getOrganizationId);
        List<SysRoleDataScope> sysRoleDataScopes = this.list(wrapper);

        if (ObjectUtil.isNotEmpty(sysRoleDataScopes)) {
            return sysRoleDataScopes.stream().map(SysRoleDataScope::getOrganizationId).collect(Collectors.toSet());
        }

        return new HashSet<>();
    }

    @Override
    public void validateHaveRoleBind(Set<Long> beRemovedRoleIdList) {
        // none
    }

    @Override
    public void removeRoleAction(Set<Long> beRemovedRoleIdList) {
        LambdaQueryWrapper<SysRoleDataScope> sysRoleDataScopeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleDataScopeLambdaQueryWrapper.in(SysRoleDataScope::getRoleId, beRemovedRoleIdList);
        this.remove(sysRoleDataScopeLambdaQueryWrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private SysRoleDataScope querySysRoleDataScope(SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        SysRoleDataScope sysRoleDataScope = this.getById(sysRoleDataScopeRequest.getRoleDataScopeId());
        if (ObjectUtil.isEmpty(sysRoleDataScope)) {
            throw new ServiceException(SysRoleDataScopeExceptionEnum.SYS_ROLE_DATA_SCOPE_NOT_EXISTED);
        }
        return sysRoleDataScope;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private LambdaQueryWrapper<SysRoleDataScope> createWrapper(SysRoleDataScopeRequest sysRoleDataScopeRequest) {
        LambdaQueryWrapper<SysRoleDataScope> queryWrapper = new LambdaQueryWrapper<>();

        Long roleId = sysRoleDataScopeRequest.getRoleId();
        queryWrapper.eq(ObjectUtil.isNotNull(roleId), SysRoleDataScope::getRoleId, roleId);

        return queryWrapper;
    }

}