package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.db.api.DbOperatorApi;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveOrgCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveUserCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.exception.enums.OrgExceptionEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserOrgDTO;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserOrg;
import cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserOrgExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.factory.UserOrgFactory;
import cn.stylefeng.roses.kernel.sys.modular.user.mapper.SysUserOrgMapper;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserOrgRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserOrgService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserOrgExceptionEnum.MAIN_FLAG_COUNT_ERROR;

/**
 * 用户组织机构关联业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@Service
@Slf4j
public class SysUserOrgServiceImpl extends ServiceImpl<SysUserOrgMapper, SysUserOrg> implements SysUserOrgService, RemoveOrgCallbackApi,
        RemoveUserCallbackApi {

    @Resource
    private DbOperatorApi dbOperatorApi;

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
    @Transactional(rollbackFor = Exception.class)
    public void updateUserOrg(Long userId, List<SysUserOrg> userOrgList) {

        // 先校验组织机构
        List<SysUserOrg> sysUserOrgResult = this.validateUserOrgParam(userId, userOrgList);

        // 删除已经绑定的组织机构
        LambdaQueryWrapper<SysUserOrg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserOrg::getUserId, userId);
        this.remove(wrapper);

        // 重新绑定用户对应的组织机构
        this.saveBatch(sysUserOrgResult);
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
            throw new ServiceException(OrgExceptionEnum.DELETE_ORGANIZATION_ERROR);
        }
    }

    @Override
    public void removeOrgAction(Set<Long> beRemovedOrgIdList) {
        LambdaQueryWrapper<SysUserOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysUserOrg::getOrgId, beRemovedOrgIdList);
        this.remove(queryWrapper);
    }

    @Override
    public void validateHaveUserBind(Set<Long> beRemovedUserIdList) {

    }

    @Override
    public void removeUserAction(Set<Long> beRemovedUserIdList) {
        LambdaQueryWrapper<SysUserOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysUserOrg::getUserId, beRemovedUserIdList);
        this.remove(queryWrapper);
    }

    @Override
    public UserOrgDTO getUserMainOrgInfo(Long userId) {

        if (userId == null) {
            return null;
        }

        LambdaQueryWrapper<SysUserOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserOrg::getUserId, userId);
        queryWrapper.eq(SysUserOrg::getMainFlag, YesOrNotEnum.Y.getCode());
        List<SysUserOrg> sysUserOrgList = this.list(queryWrapper);

        // 部门为空，直接返回null
        if (ObjectUtil.isEmpty(sysUserOrgList)) {
            return null;
        }

        if (sysUserOrgList.size() > 1) {
            log.warn(StrUtil.format(MAIN_FLAG_COUNT_ERROR.getUserTip(), userId));
        }

        // 获取到用户的主部门信息
        SysUserOrg sysUserOrg = sysUserOrgList.get(0);
        return UserOrgFactory.createUserOrgDetailInfo(sysUserOrg);
    }

    @Override
    public List<UserOrgDTO> getUserOrgList(Long userId) {

        if (userId == null) {
            return null;
        }

        // 获取用户所有的部门信息
        LambdaQueryWrapper<SysUserOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserOrg::getUserId, userId);
        queryWrapper.orderByDesc(SysUserOrg::getMainFlag);
        List<SysUserOrg> sysUserOrgList = this.list(queryWrapper);

        // 补充完整用户的部门和职位信息
        ArrayList<UserOrgDTO> userOrgDTOS = new ArrayList<>();
        for (SysUserOrg sysUserOrg : sysUserOrgList) {
            UserOrgDTO userOrgDetailInfo = UserOrgFactory.createUserOrgDetailInfo(sysUserOrg);
            userOrgDTOS.add(userOrgDetailInfo);
        }

        return userOrgDTOS;
    }

    @Override
    public List<Long> getOrgUserIdList(Long orgId, Boolean containSubOrgFlag) {

        // 如果不包含查询子公司，则直接查询参数指定公司下的人员
        if (!containSubOrgFlag) {
            LambdaQueryWrapper<SysUserOrg> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUserOrg::getOrgId, orgId);
            queryWrapper.select(SysUserOrg::getUserId);
            List<SysUserOrg> list = this.list(queryWrapper);
            return list.stream().map(SysUserOrg::getUserId).collect(Collectors.toList());
        }

        // 如果包含查询子公司，以及子公司的子公司
        Set<Long> subOrgIdList = dbOperatorApi.findSubListByParentId("hr_organization", "org_pids", "org_id", orgId);
        subOrgIdList.add(orgId);

        LambdaQueryWrapper<SysUserOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysUserOrg::getOrgId, subOrgIdList);
        queryWrapper.select(SysUserOrg::getUserId);
        List<SysUserOrg> list = this.list(queryWrapper);
        return list.stream().map(SysUserOrg::getUserId).collect(Collectors.toList());
    }

    @Override
    public boolean validateUserOrgAuth(Long orgId, Long userId) {
        LambdaQueryWrapper<SysUserOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserOrg::getUserId, userId);
        queryWrapper.eq(SysUserOrg::getOrgId, orgId);
        return this.count(queryWrapper) > 0;
    }

    @Override
    public List<Long> getPositionUserList(Long orgId, Long positionId) {

        SysUserOrgRequest sysUserOrgRequest = new SysUserOrgRequest();
        sysUserOrgRequest.setOrgId(orgId);
        sysUserOrgRequest.setPositionId(positionId);

        LambdaQueryWrapper<SysUserOrg> wrapper = this.createWrapper(sysUserOrgRequest);
        wrapper.select(SysUserOrg::getUserId);
        List<SysUserOrg> list = this.list(wrapper);

        if (ObjectUtil.isNotEmpty(list)) {
            return list.stream().map(SysUserOrg::getUserId).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
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

        Long positionId = sysUserOrgRequest.getPositionId();
        queryWrapper.eq(ObjectUtil.isNotNull(positionId), SysUserOrg::getPositionId, positionId);

        return queryWrapper;
    }

    /**
     * 校验用户绑定的组织机构是否合法
     * <p>
     * 1. 校验orgId和positionId和mainFlag是否有空的
     * 2. 校验是否有且只有一个主部门
     *
     * @return 填充好userId的正确参数，直接可以保存到库中
     * @author fengshuonan
     * @since 2023/6/11 22:26
     */
    private List<SysUserOrg> validateUserOrgParam(Long userId, List<SysUserOrg> userOrgList) {

        int mainFlagCount = 0;

        for (SysUserOrg sysUserOrg : userOrgList) {

            // 校验参数是否缺失
            if (ObjectUtil.isEmpty(sysUserOrg.getOrgId()) || ObjectUtil.isEmpty(sysUserOrg.getPositionId()) || ObjectUtil.isEmpty(
                    sysUserOrg.getMainFlag())) {
                throw new ServiceException(SysUserOrgExceptionEnum.EMPTY_PARAM);
            }

            // 统计主部门的数量
            if (YesOrNotEnum.Y.getCode().equals(sysUserOrg.getMainFlag())) {
                mainFlagCount++;
            }

            // 绑定用户id
            sysUserOrg.setUserId(userId);
        }

        if (mainFlagCount > 1 || mainFlagCount == 0) {
            throw new ServiceException(SysUserOrgExceptionEnum.MAIN_FLAG_ERROR);
        }

        return userOrgList;
    }

}