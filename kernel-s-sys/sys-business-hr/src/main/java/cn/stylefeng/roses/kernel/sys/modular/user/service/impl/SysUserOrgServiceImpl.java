package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveOrgCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.enums.HrOrganizationExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserOrg;
import cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserOrgExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.mapper.SysUserOrgMapper;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserOrgRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserOrgService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 用户组织机构关联业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@Service
public class SysUserOrgServiceImpl extends ServiceImpl<SysUserOrgMapper, SysUserOrg> implements SysUserOrgService, RemoveOrgCallbackApi {

    @Override
    public void add(SysUserOrgRequest sysUserOrgRequest) {
        SysUserOrg sysUserOrg = new SysUserOrg();
        BeanUtil.copyProperties(sysUserOrgRequest, sysUserOrg);
        this.save(sysUserOrg);
    }

    @Override
    public void del(SysUserOrgRequest sysUserOrgRequest) {
        SysUserOrg sysUserOrg = this.querySysUserOrg(sysUserOrgRequest);
        this.removeById(sysUserOrg.getUserOrgId());
    }

    @Override
    public void edit(SysUserOrgRequest sysUserOrgRequest) {
        SysUserOrg sysUserOrg = this.querySysUserOrg(sysUserOrgRequest);
        BeanUtil.copyProperties(sysUserOrgRequest, sysUserOrg);
        this.updateById(sysUserOrg);
    }

    @Override
    public SysUserOrg detail(SysUserOrgRequest sysUserOrgRequest) {
        return this.querySysUserOrg(sysUserOrgRequest);
    }

    @Override
    public PageResult<SysUserOrg> findPage(SysUserOrgRequest sysUserOrgRequest) {
        LambdaQueryWrapper<SysUserOrg> wrapper = createWrapper(sysUserOrgRequest);
        Page<SysUserOrg> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysUserOrg> findList(SysUserOrgRequest sysUserOrgRequest) {
        LambdaQueryWrapper<SysUserOrg> wrapper = this.createWrapper(sysUserOrgRequest);
        return this.list(wrapper);
    }

    @Override
    public void validateHaveOrgBind(Set<Long> beRemovedOrgIdList) {
        LambdaQueryWrapper<SysUserOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysUserOrg::getOrgId, beRemovedOrgIdList);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new ServiceException(HrOrganizationExceptionEnum.DELETE_ORGANIZATION_ERROR);
        }
    }

    @Override
    public void removeOrgAction(Set<Long> beRemovedOrgIdList) {
        LambdaQueryWrapper<SysUserOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysUserOrg::getOrgId, beRemovedOrgIdList);
        this.remove(queryWrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    private SysUserOrg querySysUserOrg(SysUserOrgRequest sysUserOrgRequest) {
        SysUserOrg sysUserOrg = this.getById(sysUserOrgRequest.getUserOrgId());
        if (ObjectUtil.isEmpty(sysUserOrg)) {
            throw new ServiceException(SysUserOrgExceptionEnum.SYS_USER_ORG_NOT_EXISTED);
        }
        return sysUserOrg;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    private LambdaQueryWrapper<SysUserOrg> createWrapper(SysUserOrgRequest sysUserOrgRequest) {
        LambdaQueryWrapper<SysUserOrg> queryWrapper = new LambdaQueryWrapper<>();

        Long orgId = sysUserOrgRequest.getOrgId();
        queryWrapper.eq(ObjectUtil.isNotNull(orgId), SysUserOrg::getOrgId, orgId);

        return queryWrapper;
    }

}