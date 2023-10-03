package cn.stylefeng.roses.kernel.auth.api.expander;

import cn.stylefeng.roses.kernel.auth.api.constants.LoginCacheConstants;
import cn.stylefeng.roses.kernel.config.api.context.ConfigContext;

/**
 * 登录相关的配置
 *
 * @author fengshuonan
 * @since 2023/10/3 20:24
 */
public class LoginConfigExpander {

    /**
     * 登录账号密码登录最大的错误次数
     * <p>
     * 超过此次数则冻结账号
     *
     * @author fengshuonan
     * @since 2023/10/3 20:25
     */
    public static Integer getMaxErrorLoginCount() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_LOGIN_MAX_ERROR_LOGIN_COUNT", Integer.class, LoginCacheConstants.MAX_ERROR_LOGIN_COUNT);
    }

}
