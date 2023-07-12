package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleDataScope;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.exception.SysRoleDataScopeExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleDataScopeMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleDataScopeRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleDataScopeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 角色数据范围业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Service
public class SysRoleDataScopeServiceImpl extends ServiceImpl<SysRoleDataScopeMapper, SysRoleDataScope> implements SysRoleDataScopeService, RemoveRoleCallbackApi {

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