package cn.stylefeng.roses.kernel.sys.modular.login.service;

import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.SimpleUserDTO;
import cn.stylefeng.roses.kernel.sys.modular.login.pojo.UserIndexInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 获取用户首页信息的业务
 *
 * @author fengshuonan
 * @since 2023/6/18 22:55
 */
@Service
public class UserIndexInfoService {

    @Resource
    private SysUserServiceApi sysUserServiceApi;

    /**
     * 获取用户首页信息
     *
     * @author fengshuonan
     * @since 2023/6/18 22:55
     */
    public UserIndexInfo getUserIndexInfo() {

        // 返回结果初始化
        UserIndexInfo userIndexInfo = new UserIndexInfo();

        // 获取当前登录用户
        LoginUser loginUser = LoginContext.me().getLoginUser();

        // 1. 获取用户的姓名和头像
        this.fillUserBaseInfo(loginUser.getUserId(), userIndexInfo);

        // 2. 获取用户的部门和任职信息

        // 3. 获取用户的权限编码集合

        // 4. 获取用户的当前登录App和菜单

        // 5. 获取菜单和路由的appId映射关系

        // 6. 构建websocket url

        return userIndexInfo;
    }

    /**
     * 填充用户的基本姓名和头像信息
     *
     * @author fengshuonan
     * @since 2023/6/18 23:01
     */
    private void fillUserBaseInfo(Long userId, UserIndexInfo userIndexInfo) {
        SimpleUserDTO simpleUserDTO = sysUserServiceApi.getUserInfoByUserId(userId);
        userIndexInfo.setUserId(simpleUserDTO.getUserId());
        userIndexInfo.setRealName(simpleUserDTO.getRealName());
        userIndexInfo.setAvatarUrl(simpleUserDTO.getAvatarUrl());
    }

}
