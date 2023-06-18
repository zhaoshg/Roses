package cn.stylefeng.roses.kernel.sys.modular.login.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.login.pojo.UserIndexInfo;
import cn.stylefeng.roses.kernel.sys.modular.login.service.UserIndexInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户信息获取的接口
 *
 * @author fengshuonan
 * @since 2023/6/18 22:08
 */
@RestController
@Slf4j
@ApiResource(name = "用户信息获取的接口")
public class UserIndexInfoController {

    @Resource
    private UserIndexInfoService userIndexInfoService;

    /**
     * 获取用户登录后的信息
     *
     * @author fengshuonan
     * @since 2023/6/18 22:08
     */
    @PostResource(name = "获取用户登录后的信息", path = "/userIndexInfo")
    public ResponseData<UserIndexInfo> userIndexInfo() {
        return new SuccessResponseData<>(userIndexInfoService.getUserIndexInfo());
    }

}
