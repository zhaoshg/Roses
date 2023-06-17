package cn.stylefeng.roses.kernel.sys.modular.org.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.db.api.context.DbOperatorContext;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.rule.tree.factory.DefaultTreeBuildFactory;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveOrgCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.enums.org.OrgTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.exception.enums.OrgExceptionEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.CompanyDeptDTO;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrganization;
import cn.stylefeng.roses.kernel.sys.modular.org.factory.OrganizationFactory;
import cn.stylefeng.roses.kernel.sys.modular.org.mapper.HrOrganizationMapper;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.HrOrganizationRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.service.HrOrganizationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        // 填充父级parentIds
        OrganizationFactory.fillParentIds(hrOrganization);

        this.save(hrOrganization);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(HrOrganizationRequest hrOrganizationRequest) {

        // 查询被删除组织机构的所有子级节点
        Set<Long> totalOrgIdSet = DbOperatorContext.me().findSubListByParentId("hr_organization", "org_pids", "org_id", hrOrganizationRequest.getOrgId());
        totalOrgIdSet.add(hrOrganizationRequest.getOrgId());

        // 执行删除操作
        this.baseDelete(totalOrgIdSet);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(HrOrganizationRequest hrOrganizationRequest) {

        Set<Long> orgIdList = hrOrganizationRequest.getOrgIdList();

        // 批量查询组织机构下的下属机构
        for (Long orgId : orgIdList) {
            // 查询被删除组织机构的所有子级节点
            Set<Long> tempSubOrgIdList = DbOperatorContext.me().findSubListByParentId("hr_organization", "org_pids", "org_id", orgId);
            orgIdList.addAll(tempSubOrgIdList);
        }

        // 执行删除操作
        this.baseDelete(orgIdList);
    }

    @Override
    public void edit(HrOrganizationRequest hrOrganizationRequest) {
        HrOrganization hrOrganization = this.queryHrOrganization(hrOrganizationRequest);
        BeanUtil.copyProperties(hrOrganizationRequest, hrOrganization);

        // 填充父级parentIds
        OrganizationFactory.fillParentIds(hrOrganization);

        this.updateById(hrOrganization);
    }

    @Override
    public HrOrganization detail(HrOrganizationRequest hrOrganizationRequest) {
        return this.queryHrOrganization(hrOrganizationRequest);
    }

    @Override
    public List<HrOrganization> findList(HrOrganizationRequest hrOrganizationRequest) {
        LambdaQueryWrapper<HrOrganization> wrapper = this.createWrapper(hrOrganizationRequest);
        return this.list(wrapper);
    }

    @Override
    public PageResult<HrOrganization> findPage(HrOrganizationRequest hrOrganizationRequest) {
        LambdaQueryWrapper<HrOrganization> wrapper = createWrapper(hrOrganizationRequest);

        // 只查询需要的字段
        wrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgName, HrOrganization::getOrgCode, HrOrganization::getStatusFlag, HrOrganization::getOrgType, HrOrganization::getOrgSort, BaseEntity::getCreateTime);

        Page<HrOrganization> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<HrOrganization> commonOrgTree(HrOrganizationRequest hrOrganizationRequest) {

        // 根据条件查询组织机构列表
        LambdaQueryWrapper<HrOrganization> wrapper = this.createWrapper(hrOrganizationRequest);
        wrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgParentId, HrOrganization::getOrgPids, HrOrganization::getOrgName, HrOrganization::getOrgSort, HrOrganization::getOrgType);
        List<HrOrganization> hrOrganizationList = this.list(wrapper);

        if (ObjectUtil.isEmpty(hrOrganizationList)) {
            return hrOrganizationList;
        }

        // 如果查询条件不为空，则把相关的查询结果的父级也查询出来，组成一颗完整树
        String searchText = hrOrganizationRequest.getSearchText();
        List<HrOrganization> parentOrgList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(searchText)) {
            Set<Long> orgParentIdList = OrganizationFactory.getOrgParentIdList(hrOrganizationList);
            LambdaQueryWrapper<HrOrganization> parentWrapper = new LambdaQueryWrapper<>();
            parentWrapper.in(HrOrganization::getOrgId, orgParentIdList);
            parentOrgList = this.list(parentWrapper);
        }

        // 合并两个集合
        hrOrganizationList.addAll(parentOrgList);

        // 构建树形结构
        return new DefaultTreeBuildFactory<HrOrganization>().doTreeBuild(hrOrganizationList);
    }

    @Override
    public CompanyDeptDTO getCompanyDeptInfo(Long orgId) {

        if (orgId == null) {
            return null;
        }

        HrOrganization hrOrganization = this.getById(orgId);
        if (hrOrganization == null) {
            return null;
        }

        // 获取当前组织机构id是公司还是部门，如果是公司，则直接返回结果
        if (OrgTypeEnum.COMPANY.getCode().equals(hrOrganization.getOrgType())) {
            return new CompanyDeptDTO(hrOrganization.getOrgId(), hrOrganization.getOrgName());
        }

        // 如果是部门，则递归向上查询到部门所属的公司id
        CompanyDeptDTO orgCompanyInfo = this.getOrgCompanyInfo(hrOrganization);
        if (orgCompanyInfo != null) {
            orgCompanyInfo.setDeptId(hrOrganization.getOrgId());
            orgCompanyInfo.setDeptName(hrOrganization.getOrgName());
        }

        return orgCompanyInfo;
    }

    @Override
    public CompanyDeptDTO getOrgCompanyInfo(HrOrganization hrOrganization) {

        if (hrOrganization == null) {
            return null;
        }

        // 如果是到了根节点，则直接返回当前根节点信息
        if (TreeConstants.DEFAULT_PARENT_ID.equals(hrOrganization.getOrgParentId())) {
            return new CompanyDeptDTO(hrOrganization.getOrgId(), hrOrganization.getOrgName());
        }

        // 如果当前已经是公司类型，则直接返回
        if (OrgTypeEnum.COMPANY.getCode().equals(hrOrganization.getOrgType())) {
            return new CompanyDeptDTO(hrOrganization.getOrgId(), hrOrganization.getOrgName());
        }

        // 查询父级是否是公司
        Long orgParentId = hrOrganization.getOrgParentId();
        HrOrganization parentOrgInfo = this.getById(orgParentId);
        return this.getOrgCompanyInfo(parentOrgInfo);
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
            throw new ServiceException(OrgExceptionEnum.HR_ORGANIZATION_NOT_EXISTED);
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

        // 如果按文本查询条件不为空，则判断组织机构名称、简称、税号、备注是否有匹配
        String searchText = hrOrganizationRequest.getSearchText();
        if (StrUtil.isNotEmpty(searchText)) {
            queryWrapper.like(HrOrganization::getOrgName, searchText);
            queryWrapper.or().like(HrOrganization::getOrgShortName, searchText);
            queryWrapper.or().like(HrOrganization::getTaxNo, searchText);
            queryWrapper.or().like(HrOrganization::getOrgCode, searchText);
            queryWrapper.or().like(HrOrganization::getRemark, searchText);
        }

        // 根据机构状态查询
        Integer statusFlag = hrOrganizationRequest.getStatusFlag();
        queryWrapper.eq(ObjectUtil.isNotNull(statusFlag), HrOrganization::getStatusFlag, statusFlag);

        // 根据排序正序查询
        queryWrapper.orderByAsc(HrOrganization::getOrgSort);

        return queryWrapper;
    }

    /**
     * 批量删除组织机构
     *
     * @author fengshuonan
     * @since 2023/6/11 17:00
     */
    private void baseDelete(Set<Long> totalOrgIdSet) {
        // 判断业务是否和组织机构有绑定关系
        Map<String, RemoveOrgCallbackApi> callbackApiMap = SpringUtil.getBeansOfType(RemoveOrgCallbackApi.class);
        for (RemoveOrgCallbackApi removeOrgCallbackApi : callbackApiMap.values()) {
            removeOrgCallbackApi.validateHaveOrgBind(totalOrgIdSet);
        }

        // 联动删除所有和本组织机构相关其他业务数据
        for (RemoveOrgCallbackApi removeOrgCallbackApi : callbackApiMap.values()) {
            removeOrgCallbackApi.removeOrgAction(totalOrgIdSet);
        }

        // 批量删除所有相关节点
        this.removeBatchByIds(totalOrgIdSet);
    }

}