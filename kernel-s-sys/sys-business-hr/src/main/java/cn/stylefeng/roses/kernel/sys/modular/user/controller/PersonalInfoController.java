package cn.stylefeng.roses.kernel.sys.modular.user.controller;

import cn.stylefeng.roses.kernel.rule.annotation.BusinessLog;
import cn.stylefeng.roses.kernel.rule.enums.ResBizTypeEnum;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 个人信息控制器
 *
 * @author fengshuonan
 * @date 2021/3/17 22:05
 */
@RestController
@ApiResource(name = "个人信息", resBizType = ResBizTypeEnum.SYSTEM)
public class PersonalInfoController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 更新个人信息
     *
     * @author fengshuonan
     * @since 2023/6/26 22:24
     */
    @PostResource(name = "更新个人信息", path = "/personalInfo/updateInfo")
    @BusinessLog
    public ResponseData<?> updateInfo(@RequestBody @Validated(SysUserRequest.updateInfo.class) SysUserRequest sysUserRequest) {
        sysUserService.editInfo(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 修改个人头像
     *
     * @author fengshuonan
     * @since 2023/6/26 22:24
     */
    @PostResource(name = "修改个人头像", path = "/personalInfo/updateAvatar")
    @BusinessLog
    public ResponseData<?> updateAvatar(@RequestBody @Validated(SysUserRequest.updateAvatar.class) SysUserRequest sysUserRequest) {
        sysUserService.editAvatar(sysUserRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 修改个人密码
     *
     * @author fengshuonan
     * @since 2023/6/26 22:24
     */
    @PostResource(name = "修改个人密码", path = "/personalInfo/updatePassword")
    @BusinessLog
    public ResponseData<?> updatePwd(@RequestBody @Validated(SysUserRequest.updatePwd.class) SysUserRequest sysUserRequest) {
        sysUserService.editPassword(sysUserRequest);
        return new SuccessResponseData<>();
    }

}
