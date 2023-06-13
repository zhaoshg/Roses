package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveRoleCallbackApi;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleResource;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.exception.SysRoleResourceExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleResourceMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleResourceRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleResourceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 角色资源关联业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Service
public class SysRoleResourceServiceImpl extends ServiceImpl<SysRoleResourceMapper, SysRoleResource> implements SysRoleResourceService, RemoveRoleCallbackApi {

    @Override
    public void add(SysRoleResourceRequest sysRoleResourceRequest) {
        SysRoleResource sysRoleResource = new SysRoleResource();
        BeanUtil.copyProperties(sysRoleResourceRequest, sysRoleResource);
        this.save(sysRoleResource);
    }

    @Override
    public void del(SysRoleResourceRequest sysRoleResourceRequest) {
        SysRoleResource sysRoleResource = this.querySysRoleResource(sysRoleResourceRequest);
        this.removeById(sysRoleResource.getRoleResourceId());
    }

    @Override
    public void edit(SysRoleResourceRequest sysRoleResourceRequest) {
        SysRoleResource sysRoleResource = this.querySysRoleResource(sysRoleResourceRequest);
        BeanUtil.copyProperties(sysRoleResourceRequest, sysRoleResource);
        this.updateById(sysRoleResource);
    }

    @Override
    public SysRoleResource detail(SysRoleResourceRequest sysRoleResourceRequest) {
        return this.querySysRoleResource(sysRoleResourceRequest);
    }

    @Override
    public PageResult<SysRoleResource> findPage(SysRoleResourceRequest sysRoleResourceRequest) {
        LambdaQueryWrapper<SysRoleResource> wrapper = createWrapper(sysRoleResourceRequest);
        Page<SysRoleResource> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysRoleResource> findList(SysRoleResourceRequest sysRoleResourceRequest) {
        LambdaQueryWrapper<SysRoleResource> wrapper = this.createWrapper(sysRoleResourceRequest);
        return this.list(wrapper);
    }

    @Override
    public void validateHaveRoleBind(Set<Long> beRemovedRoleIdList) {
        // none
    }

    @Override
    public void removeRoleAction(Set<Long> beRemovedRoleIdList) {
        LambdaQueryWrapper<SysRoleResource> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysRoleResource::getRoleId, beRemovedRoleIdList);
        this.remove(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private SysRoleResource querySysRoleResource(SysRoleResourceRequest sysRoleResourceRequest) {
        SysRoleResource sysRoleResource = this.getById(sysRoleResourceRequest.getRoleResourceId());
        if (ObjectUtil.isEmpty(sysRoleResource)) {
            throw new ServiceException(SysRoleResourceExceptionEnum.SYS_ROLE_RESOURCE_NOT_EXISTED);
        }
        return sysRoleResource;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private LambdaQueryWrapper<SysRoleResource> createWrapper(SysRoleResourceRequest sysRoleResourceRequest) {
        LambdaQueryWrapper<SysRoleResource> queryWrapper = new LambdaQueryWrapper<>();

        Long roleResourceId = sysRoleResourceRequest.getRoleResourceId();
        Long roleId = sysRoleResourceRequest.getRoleId();
        String resourceCode = sysRoleResourceRequest.getResourceCode();
        Integer resourceBizType = sysRoleResourceRequest.getResourceBizType();

        queryWrapper.eq(ObjectUtil.isNotNull(roleResourceId), SysRoleResource::getRoleResourceId, roleResourceId);
        queryWrapper.eq(ObjectUtil.isNotNull(roleId), SysRoleResource::getRoleId, roleId);
        queryWrapper.like(ObjectUtil.isNotEmpty(resourceCode), SysRoleResource::getResourceCode, resourceCode);
        queryWrapper.eq(ObjectUtil.isNotNull(resourceBizType), SysRoleResource::getResourceBizType, resourceBizType);

        return queryWrapper;
    }

}