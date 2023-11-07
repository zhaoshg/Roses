package cn.stylefeng.roses.kernel.sys.modular.login.controller;

import cn.stylefeng.roses.kernel.auth.api.AuthServiceApi;
import cn.stylefeng.roses.kernel.auth.api.pojo.auth.LoginResponse;
import cn.stylefeng.roses.kernel.auth.api.pojo.sso.LoginBySsoTokenRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 单点登录接口
 *
 * @author fengshuonan
 * @since 2023/11/7 14:06
 */
@RestController
@Slf4j
@ApiResource(name = "单点登录接口")
public class SsoLoginController {

    @Resource
    private AuthServiceApi authServiceApi;

    /**
     * 通过单带登录
     *
     * @author fengshuonan
     * @since 2023/11/7 14:12
     */
    @PostResource(name = "系统登录接口", path = "/loginByCaToken", requiredLogin = false)
    public ResponseData<LoginResponse> loginApi(@RequestBody @Validated LoginBySsoTokenRequest loginWithTokenRequest) {
        LoginResponse loginResponse = authServiceApi.LoginByCaToken(loginWithTokenRequest);
        return new SuccessResponseData<>(loginResponse);
    }

}
