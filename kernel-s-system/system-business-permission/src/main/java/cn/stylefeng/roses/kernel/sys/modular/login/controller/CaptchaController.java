package cn.stylefeng.roses.kernel.sys.modular.login.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.security.api.DragCaptchaApi;
import cn.stylefeng.roses.kernel.security.api.pojo.DragCaptchaImageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 图形验证码接口
 *
 * @author fengshuonan
 * @since 2023/11/24 23:46
 */
@RestController
@Slf4j
@ApiResource(name = "图形验证码接口")
public class CaptchaController {

    @Resource
    private DragCaptchaApi dragCaptchaApi;

    /**
     * 获取拖拽验证码的图片
     *
     * @author fengshuonan
     * @since 2023/11/24 23:47
     */
    @GetResource(name = "获取拖拽验证码的图片", path = "/getDragCaptcha", requiredLogin = false)
    public ResponseData<DragCaptchaImageDTO> getDragCaptcha() {
        DragCaptchaImageDTO captcha = dragCaptchaApi.createCaptcha();
        captcha.setLocationX(null);
        captcha.setLocationY(null);
        return new SuccessResponseData<>(captcha);
    }

}
