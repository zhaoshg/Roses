package cn.stylefeng.roses.kernel.sys.modular.security.service.impl;

import cn.stylefeng.roses.kernel.auth.api.expander.LoginConfigExpander;
import cn.stylefeng.roses.kernel.config.api.ConfigServiceApi;
import cn.stylefeng.roses.kernel.sys.modular.security.pojo.SecurityConfig;
import cn.stylefeng.roses.kernel.sys.modular.security.service.SecurityConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 系统配置的业务
 *
 * @author fengshuonan
 * @since 2023/10/4 16:24
 */
@Service
public class SecurityConfigServiceImpl implements SecurityConfigService {

    @Resource
    private ConfigServiceApi configServiceApi;

    @Override
    public SecurityConfig getSecurityConfig() {

        SecurityConfig securityConfig = new SecurityConfig();
        securityConfig.setMaxErrorLoginCount(LoginConfigExpander.getMaxErrorLoginCount());
        securityConfig.setMinPasswordLength(LoginConfigExpander.getMinPasswordLength());

        securityConfig.setPasswordMinSpecialSymbolCount(LoginConfigExpander.getPasswordMinSpecialSymbolCount());
        securityConfig.setGetPasswordMinUpperCaseCount(LoginConfigExpander.getPasswordMinUpperCaseCount());
        securityConfig.setPasswordMinLowerCaseCount(LoginConfigExpander.getPasswordMinLowerCaseCount());
        securityConfig.setPasswordMinNumberCount(LoginConfigExpander.getPasswordMinNumberCount());

        securityConfig.setPasswordMinUpdateDays(LoginConfigExpander.getPasswordMinUpdateDays());
        securityConfig.setPasswordMinCantRepeatTimes(LoginConfigExpander.getPasswordMinCantRepeatTimes());

        return securityConfig;
    }

    @Override
    public void updateSecurityConfig(SecurityConfig securityConfig) {

        configServiceApi.updateConfigByCode(LoginConfigExpander.SYS_LOGIN_MAX_ERROR_LOGIN_COUNT,
                String.valueOf(securityConfig.getMaxErrorLoginCount()));

        configServiceApi.updateConfigByCode(LoginConfigExpander.SYS_LOGIN_MIN_PASSWORD_LENGTH,
                String.valueOf(securityConfig.getMinPasswordLength()));

        configServiceApi.updateConfigByCode(LoginConfigExpander.SYS_LOGIN_PASSWORD_MIN_SPECIAL_SYMBOL_COUNT,
                String.valueOf(securityConfig.getPasswordMinSpecialSymbolCount()));

        configServiceApi.updateConfigByCode(LoginConfigExpander.SYS_LOGIN_PASSWORD_MIN_UPPER_CASE_COUNT,
                String.valueOf(securityConfig.getGetPasswordMinUpperCaseCount()));

        configServiceApi.updateConfigByCode(LoginConfigExpander.SYS_LOGIN_PASSWORD_MIN_LOWER_CASE_COUNT,
                String.valueOf(securityConfig.getPasswordMinLowerCaseCount()));

        configServiceApi.updateConfigByCode(LoginConfigExpander.SYS_LOGIN_PASSWORD_MIN_NUMBER_COUNT,
                String.valueOf(securityConfig.getPasswordMinNumberCount()));

        configServiceApi.updateConfigByCode(LoginConfigExpander.SYS_LOGIN_PASSWORD_MIN_UPDATE_DAYS,
                String.valueOf(securityConfig.getPasswordMinUpdateDays()));

        configServiceApi.updateConfigByCode(LoginConfigExpander.SYS_LOGIN_PASSWORD_MIN_CANT_REPEAT_TIMES,
                String.valueOf(securityConfig.getPasswordMinCantRepeatTimes()));
    }

}

