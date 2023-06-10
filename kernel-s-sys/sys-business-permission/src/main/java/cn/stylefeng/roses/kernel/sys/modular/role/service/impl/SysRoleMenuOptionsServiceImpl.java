package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.SysRoleMenuOptionsExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleMenuOptionsMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleMenuOptionsRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色和菜单下的功能关联业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Service
public class SysRoleMenuOptionsServiceImpl extends ServiceImpl<SysRoleMenuOptionsMapper, SysRoleMenuOptions> implements SysRoleMenuOptionsService {

	@Override
    public void add(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        SysRoleMenuOptions sysRoleMenuOptions = new SysRoleMenuOptions();
        BeanUtil.copyProperties(sysRoleMenuOptionsRequest, sysRoleMenuOptions);
        this.save(sysRoleMenuOptions);
    }

    @Override
    public void del(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        SysRoleMenuOptions sysRoleMenuOptions = this.querySysRoleMenuOptions(sysRoleMenuOptionsRequest);
        this.removeById(sysRoleMenuOptions.getRoleMenuOptionId());
    }

    @Override
    public void edit(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        SysRoleMenuOptions sysRoleMenuOptions = this.querySysRoleMenuOptions(sysRoleMenuOptionsRequest);
        BeanUtil.copyProperties(sysRoleMenuOptionsRequest, sysRoleMenuOptions);
        this.updateById(sysRoleMenuOptions);
    }

    @Override
    public SysRoleMenuOptions detail(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        return this.querySysRoleMenuOptions(sysRoleMenuOptionsRequest);
    }

    @Override
    public PageResult<SysRoleMenuOptions> findPage(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        LambdaQueryWrapper<SysRoleMenuOptions> wrapper = createWrapper(sysRoleMenuOptionsRequest);
        Page<SysRoleMenuOptions> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysRoleMenuOptions> findList(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        LambdaQueryWrapper<SysRoleMenuOptions> wrapper = this.createWrapper(sysRoleMenuOptionsRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private SysRoleMenuOptions querySysRoleMenuOptions(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        SysRoleMenuOptions sysRoleMenuOptions = this.getById(sysRoleMenuOptionsRequest.getRoleMenuOptionId());
        if (ObjectUtil.isEmpty(sysRoleMenuOptions)) {
            throw new ServiceException(SysRoleMenuOptionsExceptionEnum.SYS_ROLE_MENU_OPTIONS_NOT_EXISTED);
        }
        return sysRoleMenuOptions;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private LambdaQueryWrapper<SysRoleMenuOptions> createWrapper(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest) {
        LambdaQueryWrapper<SysRoleMenuOptions> queryWrapper = new LambdaQueryWrapper<>();

        Long roleMenuOptionId = sysRoleMenuOptionsRequest.getRoleMenuOptionId();
        Long roleId = sysRoleMenuOptionsRequest.getRoleId();
        Long menuOptionId = sysRoleMenuOptionsRequest.getMenuOptionId();
        Long tenantId = sysRoleMenuOptionsRequest.getTenantId();

        queryWrapper.eq(ObjectUtil.isNotNull(roleMenuOptionId), SysRoleMenuOptions::getRoleMenuOptionId, roleMenuOptionId);
        queryWrapper.eq(ObjectUtil.isNotNull(roleId), SysRoleMenuOptions::getRoleId, roleId);
        queryWrapper.eq(ObjectUtil.isNotNull(menuOptionId), SysRoleMenuOptions::getMenuOptionId, menuOptionId);
        queryWrapper.eq(ObjectUtil.isNotNull(tenantId), SysRoleMenuOptions::getTenantId, tenantId);

        return queryWrapper;
    }

}