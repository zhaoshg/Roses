package cn.stylefeng.roses.kernel.sys.modular.menu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveMenuCallbackApi;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuResource;
import cn.stylefeng.roses.kernel.sys.modular.menu.enums.SysMenuResourceExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.menu.mapper.SysMenuResourceMapper;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuResourceRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuResourceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 菜单资源绑定业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@Service
public class SysMenuResourceServiceImpl extends ServiceImpl<SysMenuResourceMapper, SysMenuResource> implements SysMenuResourceService,
        RemoveMenuCallbackApi {

    @Override
    public void add(SysMenuResourceRequest sysMenuResourceRequest) {
        SysMenuResource sysMenuResource = new SysMenuResource();
        BeanUtil.copyProperties(sysMenuResourceRequest, sysMenuResource);
        this.save(sysMenuResource);
    }

    @Override
    public void del(SysMenuResourceRequest sysMenuResourceRequest) {
        SysMenuResource sysMenuResource = this.querySysMenuResource(sysMenuResourceRequest);
        this.removeById(sysMenuResource.getMenuResourceId());
    }

    @Override
    public void edit(SysMenuResourceRequest sysMenuResourceRequest) {
        SysMenuResource sysMenuResource = this.querySysMenuResource(sysMenuResourceRequest);
        BeanUtil.copyProperties(sysMenuResourceRequest, sysMenuResource);
        this.updateById(sysMenuResource);
    }

    @Override
    public SysMenuResource detail(SysMenuResourceRequest sysMenuResourceRequest) {
        return this.querySysMenuResource(sysMenuResourceRequest);
    }

    @Override
    public PageResult<SysMenuResource> findPage(SysMenuResourceRequest sysMenuResourceRequest) {
        LambdaQueryWrapper<SysMenuResource> wrapper = createWrapper(sysMenuResourceRequest);
        Page<SysMenuResource> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysMenuResource> findList(SysMenuResourceRequest sysMenuResourceRequest) {
        LambdaQueryWrapper<SysMenuResource> wrapper = this.createWrapper(sysMenuResourceRequest);
        return this.list(wrapper);
    }

    @Override
    public void removeMenuAction(Set<Long> beRemovedMenuIdList) {
        LambdaQueryWrapper<SysMenuResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysMenuResource::getBusinessId, beRemovedMenuIdList);
        this.remove(queryWrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    private SysMenuResource querySysMenuResource(SysMenuResourceRequest sysMenuResourceRequest) {
        SysMenuResource sysMenuResource = this.getById(sysMenuResourceRequest.getMenuResourceId());
        if (ObjectUtil.isEmpty(sysMenuResource)) {
            throw new ServiceException(SysMenuResourceExceptionEnum.SYS_MENU_RESOURCE_NOT_EXISTED);
        }
        return sysMenuResource;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    private LambdaQueryWrapper<SysMenuResource> createWrapper(SysMenuResourceRequest sysMenuResourceRequest) {
        LambdaQueryWrapper<SysMenuResource> queryWrapper = new LambdaQueryWrapper<>();
        return queryWrapper;
    }

}