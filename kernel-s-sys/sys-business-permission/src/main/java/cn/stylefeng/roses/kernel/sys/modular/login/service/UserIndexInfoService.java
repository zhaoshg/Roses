package cn.stylefeng.roses.kernel.sys.modular.login.service;

import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.sys.modular.login.pojo.UserIndexInfo;
import org.springframework.stereotype.Service;

/**
 * 获取用户首页信息的业务
 *
 * @author fengshuonan
 * @since 2023/6/18 22:55
 */
@Service
public class UserIndexInfoService {

    /**
     * 获取用户首页信息
     *
     * @author fengshuonan
     * @since 2023/6/18 22:55
     */
    public UserIndexInfo getUserIndexInfo() {

        // 获取当前登录用户
        LoginUser loginUser = LoginContext.me().getLoginUser();


        return null;
    }

}
