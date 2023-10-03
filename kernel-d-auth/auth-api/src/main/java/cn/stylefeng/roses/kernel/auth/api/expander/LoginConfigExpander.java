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

    /**
     * 获取单账号单端登录的开关
     * <p>
     * 单账号单端登录为限制一个账号多个浏览器登录
     *
     * @return true-开启单端限制，false-关闭单端限制
     * @author fengshuonan
     * @since 2020/10/21 14:31
     */
    public static boolean getSingleAccountLoginFlag() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_SINGLE_ACCOUNT_LOGIN_FLAG", Boolean.class, false);
    }

    /**
     * 登录密码是否进行RSA加密校验，默认关闭
     * <p>
     * 需要前端配合加密后再打开开关
     *
     * @author fengshuonan
     * @since 2022/10/16 23:28
     */
    public static Boolean getPasswordRsaValidateFlag() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_AUTH_PASSWORD_RSA_VALIDATE", Boolean.class, false);
    }

}
