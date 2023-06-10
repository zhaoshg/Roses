package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRole;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.SysRoleExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 系统角色业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

	@Override
    public void add(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = new SysRole();
        BeanUtil.copyProperties(sysRoleRequest, sysRole);
        this.save(sysRole);
    }

    @Override
    public void del(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);
        this.removeById(sysRole.getRoleId());
    }

    @Override
    public void edit(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);
        BeanUtil.copyProperties(sysRoleRequest, sysRole);
        this.updateById(sysRole);
    }

    @Override
    public SysRole detail(SysRoleRequest sysRoleRequest) {
        return this.querySysRole(sysRoleRequest);
    }

    @Override
    public PageResult<SysRole> findPage(SysRoleRequest sysRoleRequest) {
        LambdaQueryWrapper<SysRole> wrapper = createWrapper(sysRoleRequest);
        Page<SysRole> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysRole> findList(SysRoleRequest sysRoleRequest) {
        LambdaQueryWrapper<SysRole> wrapper = this.createWrapper(sysRoleRequest);
        return this.list(wrapper);
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

        Long roleId = sysRoleRequest.getRoleId();
        String roleName = sysRoleRequest.getRoleName();
        String roleCode = sysRoleRequest.getRoleCode();
        BigDecimal roleSort = sysRoleRequest.getRoleSort();
        Integer dataScopeType = sysRoleRequest.getDataScopeType();
        Integer statusFlag = sysRoleRequest.getStatusFlag();
        String remark = sysRoleRequest.getRemark();
        String roleSystemFlag = sysRoleRequest.getRoleSystemFlag();
        String expandField = sysRoleRequest.getExpandField();
        Long versionFlag = sysRoleRequest.getVersionFlag();
        String delFlag = sysRoleRequest.getDelFlag();
        Long tenantId = sysRoleRequest.getTenantId();

        queryWrapper.eq(ObjectUtil.isNotNull(roleId), SysRole::getRoleId, roleId);
        queryWrapper.like(ObjectUtil.isNotEmpty(roleName), SysRole::getRoleName, roleName);
        queryWrapper.like(ObjectUtil.isNotEmpty(roleCode), SysRole::getRoleCode, roleCode);
        queryWrapper.eq(ObjectUtil.isNotNull(roleSort), SysRole::getRoleSort, roleSort);
        queryWrapper.eq(ObjectUtil.isNotNull(dataScopeType), SysRole::getDataScopeType, dataScopeType);
        queryWrapper.eq(ObjectUtil.isNotNull(statusFlag), SysRole::getStatusFlag, statusFlag);
        queryWrapper.like(ObjectUtil.isNotEmpty(remark), SysRole::getRemark, remark);
        queryWrapper.like(ObjectUtil.isNotEmpty(roleSystemFlag), SysRole::getRoleSystemFlag, roleSystemFlag);
        queryWrapper.like(ObjectUtil.isNotEmpty(expandField), SysRole::getExpandField, expandField);
        queryWrapper.eq(ObjectUtil.isNotNull(versionFlag), SysRole::getVersionFlag, versionFlag);
        queryWrapper.like(ObjectUtil.isNotEmpty(delFlag), SysRole::getDelFlag, delFlag);
        queryWrapper.eq(ObjectUtil.isNotNull(tenantId), SysRole::getTenantId, tenantId);

        return queryWrapper;
    }

}