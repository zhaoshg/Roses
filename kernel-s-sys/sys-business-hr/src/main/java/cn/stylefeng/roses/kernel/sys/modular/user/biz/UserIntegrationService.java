package cn.stylefeng.roses.kernel.sys.modular.user.biz;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.DbOperatorApi;
import cn.stylefeng.roses.kernel.file.api.FileInfoApi;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;
import cn.stylefeng.roses.kernel.sys.api.exception.SysException;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.SimpleUserDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserOrgDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserValidateDTO;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUser;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserOrg;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserRole;
import cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.factory.UserOrgFactory;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserOrgService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserRoleService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserOrgExceptionEnum.MAIN_FLAG_COUNT_ERROR;

/**
 * 用户相关的综合业务
 *
 * @author fengshuonan
 * @since 2023/6/11 21:44
 */
@Service
public class UserIntegrationService implements SysUserServiceApi {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private FileInfoApi fileInfoApi;

    @Resource
    private SysUserOrgService sysUserOrgService;

    @Resource
    private DbOperatorApi dbOperatorApi;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Override
    public SimpleUserDTO getUserInfoByUserId(Long userId) {

        if (ObjectUtil.isEmpty(userId)) {
            return null;
        }

        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUserId, userId);
        sysUserLambdaQueryWrapper.select(SysUser::getRealName, SysUser::getAvatar);
        SysUser sysUser = this.sysUserService.getOne(sysUserLambdaQueryWrapper);
        if (sysUser == null) {
            return null;
        }

        SimpleUserDTO simpleUserDTO = new SimpleUserDTO();
        simpleUserDTO.setUserId(userId);
        simpleUserDTO.setRealName(sysUser.getRealName());

        // 获取头像文件id信息，转化为头像URL
        Long avatarFileId = sysUser.getAvatar();
        if (avatarFileId == null) {
            return null;
        }

        // 获取头像的访问地址
        String fileAuthUrl = fileInfoApi.getFileAuthUrl(avatarFileId);
        simpleUserDTO.setAvatarUrl(fileAuthUrl);

        return simpleUserDTO;
    }

    @Override
    public UserOrgDTO getUserMainOrgInfo(Long userId) {

        if (userId == null) {
            return null;
        }

        LambdaQueryWrapper<SysUserOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserOrg::getUserId, userId);
        queryWrapper.eq(SysUserOrg::getMainFlag, YesOrNotEnum.Y.getCode());
        List<SysUserOrg> sysUserOrgList = sysUserOrgService.list(queryWrapper);
        if (sysUserOrgList.size() > 1) {
            throw new SysException(MAIN_FLAG_COUNT_ERROR, userId);
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
        List<SysUserOrg> sysUserOrgList = sysUserOrgService.list(queryWrapper);

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
            List<SysUserOrg> list = this.sysUserOrgService.list(queryWrapper);
            return list.stream().map(SysUserOrg::getUserId).collect(Collectors.toList());
        }

        // 如果包含查询子公司，以及子公司的子公司
        Set<Long> subOrgIdList = dbOperatorApi.findSubListByParentId("hr_organization", "org_pids", "org_id", orgId);
        subOrgIdList.add(orgId);

        LambdaQueryWrapper<SysUserOrg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysUserOrg::getOrgId, subOrgIdList);
        queryWrapper.select(SysUserOrg::getUserId);
        List<SysUserOrg> list = this.sysUserOrgService.list(queryWrapper);
        return list.stream().map(SysUserOrg::getUserId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getUserRoleIdList(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        wrapper.select(SysUserRole::getRoleId);
        List<SysUserRole> sysUserRoleList = this.sysUserRoleService.list(wrapper);

        return sysUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }

    @Override
    public String getUserRealName(Long userId) {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.select(SysUser::getRealName);
        sysUserLambdaQueryWrapper.eq(SysUser::getUserId, userId);
        SysUser sysUser = this.sysUserService.getOne(sysUserLambdaQueryWrapper);
        if (sysUser == null) {
            return "";
        }
        return sysUser.getRealName();
    }

    @Override
    public UserValidateDTO getUserLoginValidateDTO(String account) {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getAccount, account);
        sysUserLambdaQueryWrapper.select(SysUser::getPassword, SysUser::getStatusFlag, SysUser::getUserId);
        SysUser sysUserServiceOne = this.sysUserService.getOne(sysUserLambdaQueryWrapper, false);

        if (sysUserServiceOne == null) {
            throw new ServiceException(SysUserExceptionEnum.ACCOUNT_NOT_EXIST);
        }

        return new UserValidateDTO(sysUserServiceOne.getUserId(), sysUserServiceOne.getPassword(), sysUserServiceOne.getStatusFlag());
    }

    @Override
    public void updateUserLoginInfo(Long userId, String ip) {

        if (ObjectUtil.isEmpty(userId) || ObjectUtil.isEmpty(ip)) {
            return;
        }

        LambdaUpdateWrapper<SysUser> sysUserLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        sysUserLambdaUpdateWrapper.eq(SysUser::getUserId, userId);
        sysUserLambdaUpdateWrapper.set(SysUser::getLastLoginTime, new Date());
        sysUserLambdaUpdateWrapper.set(SysUser::getLastLoginIp, ip);
        this.sysUserService.update(sysUserLambdaUpdateWrapper);
    }

}
