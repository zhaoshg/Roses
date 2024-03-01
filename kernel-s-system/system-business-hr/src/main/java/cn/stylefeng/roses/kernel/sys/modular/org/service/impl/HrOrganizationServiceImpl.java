package cn.stylefeng.roses.kernel.sys.modular.org.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.db.api.context.DbOperatorContext;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.db.mp.datascope.annotations.DataScope;
import cn.stylefeng.roses.kernel.dsctn.api.context.DataSourceContext;
import cn.stylefeng.roses.kernel.event.sdk.publish.BusinessEventPublisher;
import cn.stylefeng.roses.kernel.log.api.util.BusinessLogUtil;
import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.rule.enums.DbTypeEnum;
import cn.stylefeng.roses.kernel.rule.enums.StatusEnum;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.rule.tree.factory.DefaultTreeBuildFactory;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveOrgCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.api.enums.org.DetectModeEnum;
import cn.stylefeng.roses.kernel.sys.api.enums.org.OrgTypeEnum;
import cn.stylefeng.roses.kernel.sys.api.exception.enums.OrgExceptionEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.CompanyDeptDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.HrOrganizationDTO;
import cn.stylefeng.roses.kernel.sys.modular.org.constants.OrgConstants;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrganization;
import cn.stylefeng.roses.kernel.sys.modular.org.factory.OrganizationFactory;
import cn.stylefeng.roses.kernel.sys.modular.org.mapper.HrOrganizationMapper;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.CommonOrgTreeRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.HrOrganizationRequest;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.response.CommonOrgTreeResponse;
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

    @Resource(name = "sysOrgSubFlagCache")
    private CacheOperatorApi<Boolean> sysOrgSubFlagCache;

    @Override
    public void add(HrOrganizationRequest hrOrganizationRequest) {
        HrOrganization hrOrganization = new HrOrganization();
        BeanUtil.copyProperties(hrOrganizationRequest, hrOrganization);

        // 填充父级parentIds
        OrganizationFactory.fillParentIds(hrOrganization);

        this.save(hrOrganization);

        // 发布一个新增组织机构的事件
        BusinessEventPublisher.publishEvent(OrgConstants.ADD_ORG_EVENT, hrOrganization);

        // 记录日志
        BusinessLogUtil.setLogTitle("添加机构，机构名称：" + hrOrganizationRequest.getOrgName());
        BusinessLogUtil.addContent("新增的机构详情如下：\n", hrOrganization);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(HrOrganizationRequest hrOrganizationRequest) {

        // 查询被删除组织机构的所有子级节点
        Set<Long> totalOrgIdSet = DbOperatorContext.me()
                .findSubListByParentId("sys_hr_organization", "org_pids", "org_id", hrOrganizationRequest.getOrgId());
        totalOrgIdSet.add(hrOrganizationRequest.getOrgId());

        // 执行删除操作
        this.baseDelete(totalOrgIdSet);

        // 发布删除机构的事件
        BusinessEventPublisher.publishEvent(OrgConstants.DELETE_ORG_EVENT, null);

        // 记录日志
        BusinessLogUtil.setLogTitle("删除机构，机构ID：" + hrOrganizationRequest.getOrgId());
        BusinessLogUtil.addContent("删除机构，机构ID：", hrOrganizationRequest.getOrgId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(HrOrganizationRequest hrOrganizationRequest) {

        Set<Long> orgIdList = hrOrganizationRequest.getOrgIdList();

        // 批量查询组织机构下的下属机构
        for (Long orgId : orgIdList) {
            // 查询被删除组织机构的所有子级节点
            Set<Long> tempSubOrgIdList = DbOperatorContext.me().findSubListByParentId("sys_hr_organization", "org_pids", "org_id", orgId);
            orgIdList.addAll(tempSubOrgIdList);
        }

        // 执行删除操作
        this.baseDelete(orgIdList);

        // 发布删除机构的事件
        BusinessEventPublisher.publishEvent(OrgConstants.DELETE_ORG_EVENT, null);

        // 记录日志
        BusinessLogUtil.setLogTitle("批量删除机构");
        BusinessLogUtil.addContent("批量删除机构，id集合为：", orgIdList);
    }

    @Override
    public void edit(HrOrganizationRequest hrOrganizationRequest) {
        HrOrganization hrOrganization = this.queryHrOrganization(hrOrganizationRequest);

        BusinessLogUtil.setLogTitle("更新机构信息，机构名称为：", hrOrganization.getOrgName());
        BusinessLogUtil.addContent("更新前的机构信息为：\n", hrOrganization);

        BeanUtil.copyProperties(hrOrganizationRequest, hrOrganization);

        // 填充父级parentIds
        OrganizationFactory.fillParentIds(hrOrganization);

        this.updateById(hrOrganization);

        // 发布编辑机构事件
        BusinessEventPublisher.publishEvent(OrgConstants.EDIT_ORG_EVENT, null);

        // 记录日志
        BusinessLogUtil.addContent("更新后的机构信息为：\n", hrOrganization);

    }

    @Override
    public HrOrganization detail(HrOrganizationRequest hrOrganizationRequest) {

        LambdaQueryWrapper<HrOrganization> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(HrOrganization::getOrgId, hrOrganizationRequest.getOrgId());
        wrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgName, HrOrganization::getOrgShortName, HrOrganization::getOrgCode,
                HrOrganization::getOrgParentId, HrOrganization::getOrgSort, HrOrganization::getOrgType, HrOrganization::getStatusFlag,
                HrOrganization::getTaxNo, HrOrganization::getRemark, HrOrganization::getOrgPids);

        HrOrganization hrOrganization = this.getOne(wrapper, false);
        if (ObjectUtil.isEmpty(hrOrganization)) {
            throw new ServiceException(OrgExceptionEnum.HR_ORGANIZATION_NOT_EXISTED);
        }

        // 获取机构的上级机构名称
        String parentOrgName = this.getOrgNameById(hrOrganization.getOrgParentId());
        hrOrganization.setParentOrgName(parentOrgName);

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

        // 只查询未禁用的组织机构
        hrOrganizationRequest.setStatusFlag(StatusEnum.ENABLE.getCode());
        LambdaQueryWrapper<HrOrganization> wrapper = createWrapper(hrOrganizationRequest);

        // 只查询需要的字段
        wrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgName, HrOrganization::getOrgCode, HrOrganization::getOrgType,
                HrOrganization::getStatusFlag, HrOrganization::getOrgParentId);

        Page<HrOrganization> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);

        // 将每个机构的公司名称返回
        for (HrOrganization hrOrganization : sysRolePage.getRecords()) {
            CompanyDeptDTO companyInfo = this.getOrgCompanyInfo(hrOrganization);
            if (companyInfo == null) {
                continue;
            }
            hrOrganization.setCompanyName(companyInfo.getCompanyName());
        }

        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    @DataScope
    public CommonOrgTreeResponse commonOrgTree(CommonOrgTreeRequest commonOrgTreeRequest) {

        // 如果查询带组织机构名称的搜索，则清空其他条件
        if (ObjectUtil.isNotEmpty(commonOrgTreeRequest.getSearchText())) {
            commonOrgTreeRequest.setOrgParentId(null);
            commonOrgTreeRequest.setIndexOrgIdList(null);
        }

        // 如果查询待组织机构的状态信息，则清空parentId
        if (ObjectUtil.isNotEmpty(commonOrgTreeRequest.getIndexOrgIdList())) {
            commonOrgTreeRequest.setOrgParentId(null);
        }

        // 根据条件查询组织机构列表
        LambdaQueryWrapper<HrOrganization> wrapper = this.createCommonTreeWrapper(commonOrgTreeRequest);
        wrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgPids, HrOrganization::getOrgParentId, HrOrganization::getOrgName,
                HrOrganization::getOrgSort, HrOrganization::getOrgType);
        List<HrOrganization> hrOrganizationList = this.list(wrapper);

        if (ObjectUtil.isEmpty(hrOrganizationList)) {
            return new CommonOrgTreeResponse(hrOrganizationList, new ArrayList<>());
        }

        // 如果查询条件不为空，则把相关的查询结果的父级也查询出来，组成一颗完整树
        String searchText = commonOrgTreeRequest.getSearchText();
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
        if (ObjectUtil.isNotEmpty(commonOrgTreeRequest.getSearchText()) || ObjectUtil.isNotEmpty(
                commonOrgTreeRequest.getIndexOrgIdList())) {
            newNotRepeatList = new DefaultTreeBuildFactory<HrOrganization>().doTreeBuild(newNotRepeatList);
        }

        // 遍历所有节点，查询这些节点有没有子级，填充haveSubOrgFlag
        this.fillHaveSubFlag(newNotRepeatList);

        // 遍历这些节点，如果有children的，都展开，并搜集到数组里
        List<Long> expandOrgIds = new ArrayList<>();
        this.fillExpandFlag(newNotRepeatList, expandOrgIds);

        return new CommonOrgTreeResponse(newNotRepeatList, expandOrgIds);
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
    public HrOrganizationDTO getOrgInfo(Long orgId) {

        // 查询组织机构对应的信息
        LambdaQueryWrapper<HrOrganization> hrOrganizationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hrOrganizationLambdaQueryWrapper.eq(HrOrganization::getOrgId, orgId);
        hrOrganizationLambdaQueryWrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgParentId, HrOrganization::getOrgPids,
                HrOrganization::getOrgName, HrOrganization::getOrgShortName, HrOrganization::getOrgSort, HrOrganization::getOrgCode);
        HrOrganization hrOrganization = this.getOne(hrOrganizationLambdaQueryWrapper, false);

        if (ObjectUtil.isEmpty(hrOrganization)) {
            return new HrOrganizationDTO();
        }

        HrOrganizationDTO hrOrganizationDTO = new HrOrganizationDTO();
        BeanUtil.copyProperties(hrOrganization, hrOrganizationDTO);
        return hrOrganizationDTO;
    }

    @Override
    public List<HrOrganizationDTO> getOrgNameList(Collection<Long> orgIdList) {
        LambdaQueryWrapper<HrOrganization> hrOrganizationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hrOrganizationLambdaQueryWrapper.in(HrOrganization::getOrgId, orgIdList);
        hrOrganizationLambdaQueryWrapper.select(HrOrganization::getOrgId, HrOrganization::getOrgName);
        List<HrOrganization> list = this.list(hrOrganizationLambdaQueryWrapper);

        if (ObjectUtil.isNotEmpty(list)) {
            return BeanUtil.copyToList(list, HrOrganizationDTO.class);
        } else {
            return new ArrayList<>();
        }
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
    public Set<Long> queryOrgIdParentIdList(Set<Long> orgIdList) {

        Set<Long> parentIdListTotal = new HashSet<>();

        if (ObjectUtil.isEmpty(orgIdList)) {
            return parentIdListTotal;
        }

        // 首先加上参数的机构集合
        parentIdListTotal.addAll(orgIdList);

        LambdaQueryWrapper<HrOrganization> hrOrganizationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hrOrganizationLambdaQueryWrapper.in(HrOrganization::getOrgId, orgIdList);
        hrOrganizationLambdaQueryWrapper.select(HrOrganization::getOrgPids);
        List<HrOrganization> hrOrganizationList = this.list(hrOrganizationLambdaQueryWrapper);

        for (HrOrganization hrOrganization : hrOrganizationList) {
            String orgPids = hrOrganization.getOrgPids();
            if (ObjectUtil.isEmpty(orgPids)) {
                continue;
            }

            orgPids = orgPids.replaceAll("\\[", "");
            orgPids = orgPids.replaceAll("]", "");

            String[] split = orgPids.split(",");
            for (String pidString : split) {
                parentIdListTotal.add(Convert.toLong(pidString));
            }
        }

        return parentIdListTotal;
    }

    @Override
    public Boolean getOrgHaveSubFlag(Long orgId) {

        if (ObjectUtil.isEmpty(orgId)) {
            return false;
        }

        Boolean cacheResult = sysOrgSubFlagCache.get(orgId.toString());
        if (cacheResult != null) {
            return cacheResult;
        }

        // 查询库中是否有上级包含了本orgId
        LambdaQueryWrapper<HrOrganization> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HrOrganization::getOrgParentId, orgId);
        wrapper.select(HrOrganization::getOrgId);
        List<HrOrganization> hrOrganizationList = this.list(wrapper);

        // 查询结果加到缓存中
        if (hrOrganizationList.size() > 0) {
            // 过期时间3600秒
            sysOrgSubFlagCache.put(orgId.toString(), true, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
            return true;
        } else {
            // 过期时间3600秒
            sysOrgSubFlagCache.put(orgId.toString(), false, SysConstants.DEFAULT_SYS_CACHE_TIMEOUT_SECONDS);
            return false;
        }
    }

    @Override
    public List<SimpleDict> getOrgListName(HrOrganizationRequest hrOrganizationRequest) {

        List<SimpleDict> dictList = new ArrayList<>();

        if (ObjectUtil.isEmpty(hrOrganizationRequest) || ObjectUtil.isEmpty(hrOrganizationRequest.getOrgIdList())) {
            return dictList;
        }

        LambdaQueryWrapper<HrOrganization> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(HrOrganization::getOrgId, hrOrganizationRequest.getOrgIdList());
        wrapper.select(HrOrganization::getOrgName, HrOrganization::getOrgId, HrOrganization::getOrgCode);
        List<HrOrganization> list = this.list(wrapper);

        if (ObjectUtil.isEmpty(list)) {
            return dictList;
        }

        for (HrOrganization hrOrganization : list) {
            dictList.add(new SimpleDict(hrOrganization.getOrgId(), hrOrganization.getOrgName(), hrOrganization.getOrgCode()));
        }

        return dictList;
    }

    @Override
    public void quickBatchSaveOrg(List<HrOrganization> batchOrgList) {
        if (DbTypeEnum.MYSQL.equals(DataSourceContext.me().getCurrentDbType())) {
            this.getBaseMapper().insertBatchSomeColumn(batchOrgList);
        } else {
            this.saveBatch(batchOrgList);
        }
    }

    @Override
    public String getOrgNameById(Long orgId) {

        if (TreeConstants.DEFAULT_PARENT_ID.equals(orgId)) {
            return OrgConstants.NONE_PARENT_ORG;
        }

        if (ObjectUtil.isEmpty(orgId)) {
            return OrgConstants.NONE_PARENT_ORG;
        }

        LambdaQueryWrapper<HrOrganization> hrOrganizationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hrOrganizationLambdaQueryWrapper.eq(HrOrganization::getOrgId, orgId);
        hrOrganizationLambdaQueryWrapper.select(HrOrganization::getOrgName);
        HrOrganization one = this.getOne(hrOrganizationLambdaQueryWrapper);

        if (one != null) {
            return one.getOrgName();
        }

        return OrgConstants.NONE_PARENT_ORG;
    }

    @Override
    public Long getParentLevelOrgId(Long orgId, Integer parentLevelNum, DetectModeEnum detectModeEnum) {
        if (DetectModeEnum.TO_TOP.equals(detectModeEnum)) {
            return calcParentOrgId(orgId, parentLevelNum, true);
        } else {
            return calcParentOrgId(orgId, parentLevelNum, false);
        }
    }

    @Override
    public CompanyDeptDTO remoteGetOrgCompanyDept(Long orgId) {
        return this.getOrgCompanyInfo(orgId);
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
            queryWrapper.nested(wrap -> {
                wrap.eq(HrOrganization::getOrgParentId, orgId).or().eq(HrOrganization::getOrgId, orgId);
            });
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

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    private LambdaQueryWrapper<HrOrganization> createCommonTreeWrapper(CommonOrgTreeRequest commonOrgTreeRequest) {

        // 创建基本的wrapper
        HrOrganizationRequest hrOrganizationRequest = new HrOrganizationRequest();
        hrOrganizationRequest.setSearchText(commonOrgTreeRequest.getSearchText());
        LambdaQueryWrapper<HrOrganization> queryWrapper = this.createWrapper(hrOrganizationRequest);

        // 如果查询条件有orgId，则查询指定机构下的子机构
        Long parentId = commonOrgTreeRequest.getOrgParentId();
        if (parentId != null) {
            queryWrapper.eq(HrOrganization::getOrgParentId, parentId);
        }

        // 如果有定位状态的组织机构，则需要查询到定位的组织机构的所有子一级
        Set<Long> indexOrgIdList = commonOrgTreeRequest.getIndexOrgIdList();
        if (ObjectUtil.isNotEmpty(indexOrgIdList)) {
            Set<Long> parentIdListTotal = this.queryOrgIdParentIdList(indexOrgIdList);
            if (ObjectUtil.isNotEmpty(parentIdListTotal)) {
                queryWrapper.in(HrOrganization::getOrgParentId, parentIdListTotal);
            }
        }

        // 如果有筛选公司的标识，则只查询公司列表
        Boolean companySearchFlag = commonOrgTreeRequest.getCompanySearchFlag();
        if (ObjectUtil.isNotEmpty(companySearchFlag) && companySearchFlag) {
            queryWrapper.eq(HrOrganization::getOrgType, OrgTypeEnum.COMPANY.getCode());
        }

        // 只查询启用状态的机构
        queryWrapper.eq(HrOrganization::getStatusFlag, StatusEnum.ENABLE.getCode());

        return queryWrapper;
    }

    /**
     * 填充是否含有下级的标识
     *
     * @author fengshuonan
     * @since 2023/7/13 21:30
     */
    private void fillHaveSubFlag(List<HrOrganization> organizations) {

        if (ObjectUtil.isEmpty(organizations)) {
            return;
        }

        for (HrOrganization organization : organizations) {

            Long orgId = organization.getOrgId();

            // 查询是否包含下级，并设置标识
            Boolean orgHaveSubFlag = this.getOrgHaveSubFlag(orgId);
            organization.setHaveSubOrgFlag(orgHaveSubFlag);

            // 如果有children则将展开标识填充，并继续向下递归填充
            if (ObjectUtil.isNotEmpty(organization.getChildren())) {
                fillHaveSubFlag(organization.getChildren());
            }

        }

    }

    /**
     * 填充是否展开的标识
     * <p>
     * 判定是否展开，如果有children则展开
     *
     * @author fengshuonan
     * @since 2023/7/17 11:11
     */
    private void fillExpandFlag(List<HrOrganization> organizations, List<Long> expandOrgIds) {

        if (ObjectUtil.isEmpty(organizations)) {
            return;
        }

        for (HrOrganization organization : organizations) {

            Long orgId = organization.getOrgId();

            // 如果有children则将展开标识填充，并继续向下递归填充
            if (ObjectUtil.isNotEmpty(organization.getChildren())) {
                expandOrgIds.add(orgId);

                // 搜集子集的children的展开标识
                fillExpandFlag(organization.getChildren(), expandOrgIds);
            }
        }
    }

    /**
     * 计算获取上级组织机构id
     *
     * @param orgId          指定机构id
     * @param parentLevelNum 上级机构的层级数，从0开始，0代表不计算直接返回本身
     * @param reverse        是否反转，true-代表自下而上计算，false-代表自上而下计算
     * @author fengshuonan
     * @since 2022/10/1 11:45
     */
    private Long calcParentOrgId(Long orgId, Integer parentLevelNum, boolean reverse) {

        if (ObjectUtil.isEmpty(orgId) || ObjectUtil.isEmpty(parentLevelNum)) {
            return null;
        }

        // 如果上级层数为0，则直接返回参数的orgId，代表同级别组织机构
        if (parentLevelNum == 0) {
            return orgId;
        }

        // 获取当前部门的所有父级id
        HrOrganization hrOrganization = this.getById(orgId);
        if (hrOrganization == null || StrUtil.isEmpty(hrOrganization.getOrgPids())) {
            return null;
        }
        String orgParentIdListStr = hrOrganization.getOrgPids();

        // 去掉中括号符号
        orgParentIdListStr = orgParentIdListStr.replaceAll("\\[", "");
        orgParentIdListStr = orgParentIdListStr.replaceAll("]", "");

        // 获取所有上级id列表
        String[] orgParentIdList = orgParentIdListStr.split(",");
        if (reverse) {
            orgParentIdList = ArrayUtil.reverse(orgParentIdList);
        }

        // 先删掉id为-1的机构，因为-1是不存在的
        ArrayList<String> parentOrgIdList = new ArrayList<>();
        for (String orgIdItem : orgParentIdList) {
            if (!TreeConstants.DEFAULT_PARENT_ID.toString().equals(orgIdItem)) {
                parentOrgIdList.add(orgIdItem);
            }
        }

        // 根据请求参数，需要从parentOrgIdList获取的下标
        int needGetArrayIndex = parentLevelNum - 1;

        // parentOrgIdList最大能提供的下标
        int maxCanGetIndex = parentOrgIdList.size() - 1;

        // 如果没有最顶级的上级，则他本身就是最顶级上级
        if (maxCanGetIndex < 0) {
            return orgId;
        }

        // 根据参数传参，进行获取上级的操作
        String orgIdString;
        if (needGetArrayIndex <= (maxCanGetIndex)) {
            orgIdString = parentOrgIdList.get(needGetArrayIndex);
        } else {
            // 如果需要获取的下标，大于了最大下标
            if (reverse) {
                orgIdString = parentOrgIdList.get(maxCanGetIndex);
            } else {
                return orgId;
            }
        }
        return Long.valueOf(orgIdString);
    }

}