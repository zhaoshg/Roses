package cn.stylefeng.roses.kernel.sys.modular.org.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
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
import cn.stylefeng.roses.kernel.sys.modular.org.constants.OrgConstants;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrganization;
import cn.stylefeng.roses.kernel.sys.modular.org.factory.OrganizationFactory;
import cn.stylefeng.roses.kernel.sys.modular.org.mapper.HrOrganizationMapper;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.HrOrganizationRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.response.HomeCompanyInfo;
import cn.stylefeng.roses.kernel.sys.modular.org.service.HrOrganizationService;
import cn.stylefeng.roses.kernel.sys.modular.position.service.HrPositionService;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserOrg;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserOrgService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.stylefeng.roses.kernel.rule.constants.SymbolConstant.LEFT_SQUARE_BRACKETS;
import static cn.stylefeng.roses.kernel.rule.constants.SymbolConstant.RIGHT_SQUARE_BRACKETS;
import static java.util.stream.Collectors.toCollection;

/**
 * 组织机构信息业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
@Service
public class HrOrganizationServiceImpl extends ServiceImpl<HrOrganizationMapper, HrOrganization> implements HrOrganizationService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private HrPositionService hrPositionService;

    @Resource
    private SysUserOrgService sysUserOrgService;

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
        Set<Long> totalOrgIdSet = DbOperatorContext.me()
                .findSubListByParentId("hr_organization", "org_pids", "org_id", hrOrganizationRequest.getOrgId());
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

        LambdaQueryWrapper<HrOrganization> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(HrOrganization::getOrgId, hrOrganizationRequest.getOrgId());
        wrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgName, HrOrganization::getOrgShortName, HrOrganization::getOrgCode,
                HrOrganization::getOrgParentId, HrOrganization::getOrgSort, HrOrganization::getOrgType, HrOrganization::getStatusFlag,
                HrOrganization::getTaxNo, HrOrganization::getRemark);

        HrOrganization hrOrganization = this.getOne(wrapper, false);
        if (ObjectUtil.isEmpty(hrOrganization)) {
            throw new ServiceException(OrgExceptionEnum.HR_ORGANIZATION_NOT_EXISTED);
        }

        // 获取机构的上级机构名称
        if (TreeConstants.DEFAULT_PARENT_ID.equals(hrOrganization.getOrgParentId())) {
            hrOrganization.setParentOrgName(OrgConstants.NONE_PARENT_ORG);
        } else {
            LambdaQueryWrapper<HrOrganization> parentWrapper = new LambdaQueryWrapper<>();
            parentWrapper.eq(HrOrganization::getOrgId, hrOrganization.getOrgParentId());
            parentWrapper.select(HrOrganization::getOrgName);
            HrOrganization parentInfo = this.getOne(parentWrapper, false);
            if (parentInfo == null) {
                hrOrganization.setParentOrgName(OrgConstants.NONE_PARENT_ORG);
            } else {
                hrOrganization.setParentOrgName(parentInfo.getOrgName());
            }
        }

        return hrOrganization;
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
        wrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgName, HrOrganization::getOrgCode, HrOrganization::getStatusFlag,
                HrOrganization::getOrgType, HrOrganization::getOrgSort, BaseEntity::getCreateTime);

        Page<HrOrganization> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public PageResult<HrOrganization> commonOrgPage(HrOrganizationRequest hrOrganizationRequest) {

        LambdaQueryWrapper<HrOrganization> wrapper = createWrapper(hrOrganizationRequest);

        // 只查询需要的字段
        wrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgName, HrOrganization::getOrgCode, HrOrganization::getOrgType,
                HrOrganization::getStatusFlag, HrOrganization::getOrgParentId);

        Page<HrOrganization> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);

        // 将每个机构的公司名称返回
        for (HrOrganization hrOrganization : sysRolePage.getRecords()) {
            CompanyDeptDTO companyInfo = this.getOrgCompanyInfo(hrOrganization);
            hrOrganization.setCompanyName(companyInfo.getCompanyName());
        }

        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<HrOrganization> commonOrgTree(HrOrganizationRequest hrOrganizationRequest) {

        // 根据条件查询组织机构列表
        LambdaQueryWrapper<HrOrganization> wrapper = this.createWrapper(hrOrganizationRequest);
        wrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgParentId, HrOrganization::getOrgPids, HrOrganization::getOrgName,
                HrOrganization::getOrgSort, HrOrganization::getOrgType);
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

        // 去重
        List<HrOrganization> newNotRepeatList = hrOrganizationList.stream().collect(
                Collectors.collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(HrOrganization::getOrgId))),
                        LinkedList::new));

        // 从新排序，根据sort字段排序
        newNotRepeatList.sort(Comparator.comparing(HrOrganization::getOrgSort));

        // 构建树形结构
        return new DefaultTreeBuildFactory<HrOrganization>().doTreeBuild(newNotRepeatList);
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

        // 查到公司id之后，设置部门id则为参数orgId
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

    @Override
    public CompanyDeptDTO getOrgCompanyInfo(Long orgId) {

        if (orgId == null) {
            return null;
        }

        // 查询组织机构对应的信息
        LambdaQueryWrapper<HrOrganization> hrOrganizationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hrOrganizationLambdaQueryWrapper.eq(HrOrganization::getOrgId, orgId);
        hrOrganizationLambdaQueryWrapper.select(HrOrganization::getOrgType, HrOrganization::getOrgId, HrOrganization::getOrgParentId);
        HrOrganization hrOrganization = this.getOne(hrOrganizationLambdaQueryWrapper, false);

        if (hrOrganization == null) {
            return null;
        }

        // 查询机构对应的公司部门信息
        return this.getOrgCompanyInfo(hrOrganization);
    }

    @Override
    public HomeCompanyInfo orgStatInfo() {

        // todo 加缓存

        HomeCompanyInfo homeCompanyInfo = new HomeCompanyInfo();

        // 1. 总机构数量
        long totalOrgCount = this.count();
        homeCompanyInfo.setOrganizationNum(totalOrgCount);

        // 2. 总人员数量
        long totalUserCount = sysUserService.count();
        homeCompanyInfo.setEnterprisePersonNum(totalUserCount);

        // 3. 总职位信息
        long totalPositionCount = hrPositionService.count();
        homeCompanyInfo.setPositionNum(totalPositionCount);

        // 4. 当前公司下的机构数量
        Long currentOrgId = LoginContext.me().getLoginUser().getCurrentOrgId();
        CompanyDeptDTO orgCompanyInfo = this.getOrgCompanyInfo(currentOrgId);

        // 当前用户没公司，则直接设置为0
        if (currentOrgId == null || orgCompanyInfo == null) {
            homeCompanyInfo.setCurrentCompanyPersonNum(0L);
            homeCompanyInfo.setCurrentCompanyPersonNum(0L);
            return homeCompanyInfo;
        }

        Long companyId = orgCompanyInfo.getCompanyId();

        // 获取当前公司的所有子公司数量(含当前公司)
        LambdaQueryWrapper<HrOrganization> wrapper = Wrappers.lambdaQuery(HrOrganization.class)
                .like(HrOrganization::getOrgPids, LEFT_SQUARE_BRACKETS + companyId + RIGHT_SQUARE_BRACKETS).or()
                .eq(HrOrganization::getOrgId, companyId).select(HrOrganization::getOrgId);
        List<HrOrganization> organizations = this.list(wrapper);
        homeCompanyInfo.setCurrentDeptNum(organizations.size());

        // 5. 当前机构下的人员数量
        if (ObjectUtil.isEmpty(organizations)) {
            homeCompanyInfo.setCurrentCompanyPersonNum(0L);
        } else {
            List<Long> orgIdList = organizations.stream().map(HrOrganization::getOrgId).collect(Collectors.toList());
            LambdaQueryWrapper<SysUserOrg> userWrapper = new LambdaQueryWrapper<SysUserOrg>().in(SysUserOrg::getOrgId, orgIdList);
            userWrapper.select(SysUserOrg::getUserId);
            List<SysUserOrg> list = sysUserOrgService.list(userWrapper);
            Set<Long> currentOrgUserSize = list.stream().map(SysUserOrg::getUserId).collect(Collectors.toSet());
            homeCompanyInfo.setCurrentCompanyPersonNum(Convert.toLong(currentOrgUserSize.size()));
        }

        return homeCompanyInfo;
    }

    @Override
    public List<Long> getSubOrgIdListOneLevel(Long orgId) {
        LambdaQueryWrapper<HrOrganization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HrOrganization::getOrgParentId, orgId);
        wrapper.select(HrOrganization::getOrgId);
        List<HrOrganization> subOrgList = this.list(wrapper);

        if (ObjectUtil.isEmpty(subOrgList)) {
            return ListUtil.list(false, orgId);
        }

        List<Long> subOrgIdList = subOrgList.stream().map(HrOrganization::getOrgId).collect(Collectors.toList());
        subOrgIdList.add(orgId);

        return subOrgIdList;
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
            queryWrapper.nested(wrap -> {
                wrap.like(HrOrganization::getOrgName, searchText);
                wrap.or().like(HrOrganization::getOrgShortName, searchText);
                wrap.or().like(HrOrganization::getTaxNo, searchText);
                wrap.or().like(HrOrganization::getOrgCode, searchText);
                wrap.or().like(HrOrganization::getRemark, searchText);
            });
        }

        // 根据机构状态查询
        Integer statusFlag = hrOrganizationRequest.getStatusFlag();
        queryWrapper.eq(ObjectUtil.isNotNull(statusFlag), HrOrganization::getStatusFlag, statusFlag);

        // 如果查询条件有orgId，则查询指定机构下的子机构
        Long orgId = hrOrganizationRequest.getOrgId();
        if (orgId != null) {
            // 查询orgId对应的所有子机构，包含本orgId
            List<Long> subOrgIdListOneLevel = this.getSubOrgIdListOneLevel(orgId);
            queryWrapper.in(HrOrganization::getOrgId, subOrgIdListOneLevel);
        }

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