package cn.stylefeng.roses.kernel.sys.modular.menu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.menu.enums.SysMenuOptionsExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.menu.mapper.SysMenuOptionsMapper;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuOptionsRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单下的功能操作业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@Service
public class SysMenuOptionsServiceImpl extends ServiceImpl<SysMenuOptionsMapper, SysMenuOptions> implements SysMenuOptionsService {

	@Override
    public void add(SysMenuOptionsRequest sysMenuOptionsRequest) {
        SysMenuOptions sysMenuOptions = new SysMenuOptions();
        BeanUtil.copyProperties(sysMenuOptionsRequest, sysMenuOptions);
        this.save(sysMenuOptions);
    }

    @Override
    public void del(SysMenuOptionsRequest sysMenuOptionsRequest) {
        SysMenuOptions sysMenuOptions = this.querySysMenuOptions(sysMenuOptionsRequest);
        this.removeById(sysMenuOptions.getMenuOptionId());
    }

    @Override
    public void edit(SysMenuOptionsRequest sysMenuOptionsRequest) {
        SysMenuOptions sysMenuOptions = this.querySysMenuOptions(sysMenuOptionsRequest);
        BeanUtil.copyProperties(sysMenuOptionsRequest, sysMenuOptions);
        this.updateById(sysMenuOptions);
    }

    @Override
    public SysMenuOptions detail(SysMenuOptionsRequest sysMenuOptionsRequest) {
        return this.querySysMenuOptions(sysMenuOptionsRequest);
    }

    @Override
    public PageResult<SysMenuOptions> findPage(SysMenuOptionsRequest sysMenuOptionsRequest) {
        LambdaQueryWrapper<SysMenuOptions> wrapper = createWrapper(sysMenuOptionsRequest);
        Page<SysMenuOptions> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysMenuOptions> findList(SysMenuOptionsRequest sysMenuOptionsRequest) {
        LambdaQueryWrapper<SysMenuOptions> wrapper = this.createWrapper(sysMenuOptionsRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    private SysMenuOptions querySysMenuOptions(SysMenuOptionsRequest sysMenuOptionsRequest) {
        SysMenuOptions sysMenuOptions = this.getById(sysMenuOptionsRequest.getMenuOptionId());
        if (ObjectUtil.isEmpty(sysMenuOptions)) {
            throw new ServiceException(SysMenuOptionsExceptionEnum.SYS_MENU_OPTIONS_NOT_EXISTED);
        }
        return sysMenuOptions;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    private LambdaQueryWrapper<SysMenuOptions> createWrapper(SysMenuOptionsRequest sysMenuOptionsRequest) {
        LambdaQueryWrapper<SysMenuOptions> queryWrapper = new LambdaQueryWrapper<>();

        Long menuOptionId = sysMenuOptionsRequest.getMenuOptionId();
        Long menuId = sysMenuOptionsRequest.getMenuId();
        String optionName = sysMenuOptionsRequest.getOptionName();
        String optionCode = sysMenuOptionsRequest.getOptionCode();
        Long tenantId = sysMenuOptionsRequest.getTenantId();

        queryWrapper.eq(ObjectUtil.isNotNull(menuOptionId), SysMenuOptions::getMenuOptionId, menuOptionId);
        queryWrapper.eq(ObjectUtil.isNotNull(menuId), SysMenuOptions::getMenuId, menuId);
        queryWrapper.like(ObjectUtil.isNotEmpty(optionName), SysMenuOptions::getOptionName, optionName);
        queryWrapper.like(ObjectUtil.isNotEmpty(optionCode), SysMenuOptions::getOptionCode, optionCode);
        queryWrapper.eq(ObjectUtil.isNotNull(tenantId), SysMenuOptions::getTenantId, tenantId);

        return queryWrapper;
    }

}