package cn.stylefeng.roses.kernel.auth.starter;

import cn.stylefeng.roses.kernel.auth.api.expander.JwtConfigExpander;
import cn.stylefeng.roses.kernel.jwt.JwtTokenOperator;
import cn.stylefeng.roses.kernel.jwt.api.JwtApi;
import cn.stylefeng.roses.kernel.jwt.api.pojo.config.JwtConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auth模块对jwt的封装
 *
 * @author fengshuonan
 * @since 2023/5/9 10:07
 */
@Configuration
public class AuthJwtConfigAutoConfiguration {

    /**
     * jwt操作工具类的配置
     *
     * @author fengshuonan
     * @since 2020/12/1 14:40
     */
    @Bean
    public JwtApi jwtApi() {

        JwtConfig jwtConfig = new JwtConfig();

        // 从系统配置表中读取配置
        jwtConfig.setJwtSecret(JwtConfigExpander.getJwtSecret());
        jwtConfig.setExpiredSeconds(JwtConfigExpander.getJwtTimeoutSeconds());

        return new JwtTokenOperator(jwtConfig);
    }

}
