package cn.stylefeng.roses.kernel.sys.modular.user.biz;

import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;
import cn.stylefeng.roses.kernel.sys.api.pojo.SimpleUserDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.UserOrgDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户相关的综合业务
 *
 * @author fengshuonan
 * @since 2023/6/11 21:44
 */
@Service
public class UserIntegrationService implements SysUserServiceApi {

    @Override
    public SimpleUserDTO getUserInfoByUserId(Long userId) {
        return null;
    }

    @Override
    public UserOrgDTO getUserMainOrgInfo(Long userId) {
        return null;
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
