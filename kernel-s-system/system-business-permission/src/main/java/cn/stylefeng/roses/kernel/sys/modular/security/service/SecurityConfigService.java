package cn.stylefeng.roses.kernel.sys.modular.security.service;

import cn.stylefeng.roses.kernel.sys.modular.security.pojo.SecurityConfig;

/**
 * 系统安全配置的业务
 *
 * @author fengshuonan
 * @since 2023/10/4 16:22
 */
public interface SecurityConfigService {

    /**
     * 获取系统安全配置
     *
     * @author fengshuonan
     * @since 2023/10/4 16:23
     */
    SecurityConfig getSecurityConfig();

    /**
     * 更新系统安全配置
     *
     * @author fengshuonan
     * @since 2023/10/4 16:25
     */
    void updateSecurityConfig(SecurityConfig securityConfig);

}
