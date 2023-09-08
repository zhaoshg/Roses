package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleLimit;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.SysRoleLimitExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleLimitMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleLimitRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleLimitService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色权限限制业务实现层
 *
 * @author fengshuonan
 * @date 2023/09/08 12:55
 */
@Service
public class SysRoleLimitServiceImpl extends ServiceImpl<SysRoleLimitMapper, SysRoleLimit> implements SysRoleLimitService {

	@Override
    public void add(SysRoleLimitRequest sysRoleLimitRequest) {
        SysRoleLimit sysRoleLimit = new SysRoleLimit();
        BeanUtil.copyProperties(sysRoleLimitRequest, sysRoleLimit);
        this.save(sysRoleLimit);
    }

    @Override
    public void del(SysRoleLimitRequest sysRoleLimitRequest) {
        SysRoleLimit sysRoleLimit = this.querySysRoleLimit(sysRoleLimitRequest);
        this.removeById(sysRoleLimit.getRoleLimitId());
    }

    @Override
    public void edit(SysRoleLimitRequest sysRoleLimitRequest) {
        SysRoleLimit sysRoleLimit = this.querySysRoleLimit(sysRoleLimitRequest);
        BeanUtil.copyProperties(sysRoleLimitRequest, sysRoleLimit);
        this.updateById(sysRoleLimit);
    }

    @Override
    public SysRoleLimit detail(SysRoleLimitRequest sysRoleLimitRequest) {
        return this.querySysRoleLimit(sysRoleLimitRequest);
    }

    @Override
    public PageResult<SysRoleLimit> findPage(SysRoleLimitRequest sysRoleLimitRequest) {
        LambdaQueryWrapper<SysRoleLimit> wrapper = createWrapper(sysRoleLimitRequest);
        Page<SysRoleLimit> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysRoleLimit> findList(SysRoleLimitRequest sysRoleLimitRequest) {
        LambdaQueryWrapper<SysRoleLimit> wrapper = this.createWrapper(sysRoleLimitRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/09/08 12:55
     */
    private SysRoleLimit querySysRoleLimit(SysRoleLimitRequest sysRoleLimitRequest) {
        SysRoleLimit sysRoleLimit = this.getById(sysRoleLimitRequest.getRoleLimitId());
        if (ObjectUtil.isEmpty(sysRoleLimit)) {
            throw new ServiceException(SysRoleLimitExceptionEnum.SYS_ROLE_LIMIT_NOT_EXISTED);
        }
        return sysRoleLimit;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/09/08 12:55
     */
    private LambdaQueryWrapper<SysRoleLimit> createWrapper(SysRoleLimitRequest sysRoleLimitRequest) {
        LambdaQueryWrapper<SysRoleLimit> queryWrapper = new LambdaQueryWrapper<>();

        Long roleLimitId = sysRoleLimitRequest.getRoleLimitId();
        Long roleId = sysRoleLimitRequest.getRoleId();
        Integer limitType = sysRoleLimitRequest.getLimitType();
        Long businessId = sysRoleLimitRequest.getBusinessId();

        queryWrapper.eq(ObjectUtil.isNotNull(roleLimitId), SysRoleLimit::getRoleLimitId, roleLimitId);
        queryWrapper.eq(ObjectUtil.isNotNull(roleId), SysRoleLimit::getRoleId, roleId);
        queryWrapper.eq(ObjectUtil.isNotNull(limitType), SysRoleLimit::getLimitType, limitType);
        queryWrapper.eq(ObjectUtil.isNotNull(businessId), SysRoleLimit::getBusinessId, businessId);

        return queryWrapper;
    }

}