package cn.stylefeng.roses.kernel.sys.modular.user.biz;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.file.api.FileInfoApi;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;
import cn.stylefeng.roses.kernel.sys.api.exception.SysException;
import cn.stylefeng.roses.kernel.sys.api.pojo.SimpleUserDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.UserOrgDTO;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUser;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserOrg;
import cn.stylefeng.roses.kernel.sys.modular.user.factory.UserOrgFactory;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserOrgService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
        return null;
    }

    @Override
    public List<Long> getOrgUserIdList(Long orgId, Boolean containSubOrgFlag) {
        return null;
    }

    @Override
    public List<Long> getUserRoleIdList(Long userId) {
        return null;
    }

}
