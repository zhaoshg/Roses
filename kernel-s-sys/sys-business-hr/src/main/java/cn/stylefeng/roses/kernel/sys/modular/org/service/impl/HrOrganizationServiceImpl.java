package cn.stylefeng.roses.kernel.sys.modular.org.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrganization;
import cn.stylefeng.roses.kernel.sys.modular.org.enums.HrOrganizationExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.org.mapper.HrOrganizationMapper;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.HrOrganizationRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.service.HrOrganizationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 组织机构信息业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
@Service
public class HrOrganizationServiceImpl extends ServiceImpl<HrOrganizationMapper, HrOrganization> implements HrOrganizationService {

	@Override
    public void add(HrOrganizationRequest hrOrganizationRequest) {
        HrOrganization hrOrganization = new HrOrganization();
        BeanUtil.copyProperties(hrOrganizationRequest, hrOrganization);
        this.save(hrOrganization);
    }

    @Override
    public void del(HrOrganizationRequest hrOrganizationRequest) {
        HrOrganization hrOrganization = this.queryHrOrganization(hrOrganizationRequest);
        this.removeById(hrOrganization.getOrgId());
    }

    @Override
    public void edit(HrOrganizationRequest hrOrganizationRequest) {
        HrOrganization hrOrganization = this.queryHrOrganization(hrOrganizationRequest);
        BeanUtil.copyProperties(hrOrganizationRequest, hrOrganization);
        this.updateById(hrOrganization);
    }

    @Override
    public HrOrganization detail(HrOrganizationRequest hrOrganizationRequest) {
        return this.queryHrOrganization(hrOrganizationRequest);
    }

    @Override
    public PageResult<HrOrganization> findPage(HrOrganizationRequest hrOrganizationRequest) {
        LambdaQueryWrapper<HrOrganization> wrapper = createWrapper(hrOrganizationRequest);
        Page<HrOrganization> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<HrOrganization> findList(HrOrganizationRequest hrOrganizationRequest) {
        LambdaQueryWrapper<HrOrganization> wrapper = this.createWrapper(hrOrganizationRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    private HrOrganization queryHrOrganization(HrOrganizationRequest hrOrganizationRequest) {
        HrOrganization hrOrganization = this.getById(hrOrganizationRequest.getOrgId());
        if (ObjectUtil.isEmpty(hrOrganization)) {
            throw new ServiceException(HrOrganizationExceptionEnum.HR_ORGANIZATION_NOT_EXISTED);
        }
        return hrOrganization;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    private LambdaQueryWrapper<HrOrganization> createWrapper(HrOrganizationRequest hrOrganizationRequest) {
        LambdaQueryWrapper<HrOrganization> queryWrapper = new LambdaQueryWrapper<>();

        Long orgId = hrOrganizationRequest.getOrgId();
        Long orgParentId = hrOrganizationRequest.getOrgParentId();
        String orgPids = hrOrganizationRequest.getOrgPids();
        String orgName = hrOrganizationRequest.getOrgName();
        String orgShortName = hrOrganizationRequest.getOrgShortName();
        String orgCode = hrOrganizationRequest.getOrgCode();
        BigDecimal orgSort = hrOrganizationRequest.getOrgSort();
        Integer statusFlag = hrOrganizationRequest.getStatusFlag();
        Integer orgType = hrOrganizationRequest.getOrgType();
        String taxNo = hrOrganizationRequest.getTaxNo();
        String remark = hrOrganizationRequest.getRemark();
        Integer orgLevel = hrOrganizationRequest.getOrgLevel();
        String masterOrgId = hrOrganizationRequest.getMasterOrgId();
        String masterOrgParentId = hrOrganizationRequest.getMasterOrgParentId();
        String expandField = hrOrganizationRequest.getExpandField();
        Long versionFlag = hrOrganizationRequest.getVersionFlag();
        String delFlag = hrOrganizationRequest.getDelFlag();
        Long tenantId = hrOrganizationRequest.getTenantId();

        queryWrapper.eq(ObjectUtil.isNotNull(orgId), HrOrganization::getOrgId, orgId);
        queryWrapper.eq(ObjectUtil.isNotNull(orgParentId), HrOrganization::getOrgParentId, orgParentId);
        queryWrapper.like(ObjectUtil.isNotEmpty(orgPids), HrOrganization::getOrgPids, orgPids);
        queryWrapper.like(ObjectUtil.isNotEmpty(orgName), HrOrganization::getOrgName, orgName);
        queryWrapper.like(ObjectUtil.isNotEmpty(orgShortName), HrOrganization::getOrgShortName, orgShortName);
        queryWrapper.like(ObjectUtil.isNotEmpty(orgCode), HrOrganization::getOrgCode, orgCode);
        queryWrapper.eq(ObjectUtil.isNotNull(orgSort), HrOrganization::getOrgSort, orgSort);
        queryWrapper.eq(ObjectUtil.isNotNull(statusFlag), HrOrganization::getStatusFlag, statusFlag);
        queryWrapper.eq(ObjectUtil.isNotNull(orgType), HrOrganization::getOrgType, orgType);
        queryWrapper.like(ObjectUtil.isNotEmpty(taxNo), HrOrganization::getTaxNo, taxNo);
        queryWrapper.like(ObjectUtil.isNotEmpty(remark), HrOrganization::getRemark, remark);
        queryWrapper.eq(ObjectUtil.isNotNull(orgLevel), HrOrganization::getOrgLevel, orgLevel);
        queryWrapper.like(ObjectUtil.isNotEmpty(masterOrgId), HrOrganization::getMasterOrgId, masterOrgId);
        queryWrapper.like(ObjectUtil.isNotEmpty(masterOrgParentId), HrOrganization::getMasterOrgParentId, masterOrgParentId);
        queryWrapper.like(ObjectUtil.isNotEmpty(expandField), HrOrganization::getExpandField, expandField);
        queryWrapper.eq(ObjectUtil.isNotNull(versionFlag), HrOrganization::getVersionFlag, versionFlag);
        queryWrapper.like(ObjectUtil.isNotEmpty(delFlag), HrOrganization::getDelFlag, delFlag);
        queryWrapper.eq(ObjectUtil.isNotNull(tenantId), HrOrganization::getTenantId, tenantId);

        return queryWrapper;
    }

}