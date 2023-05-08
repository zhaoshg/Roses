package cn.stylefeng.roses.kernel.system.modular.user.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 用户机构异常
 *
 * @author fengshuonan
 * @since 2023/4/17 18:51
 */
@Getter
public enum SysUserOrgExceptionEnum implements AbstractExceptionEnum {

    /**
     * 无法切换用户组织机构，组织机构id无效
     */
    CANT_CHANGE_ORG_ID(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "无法切换用户组织机构，组织机构id无效");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SysUserOrgExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}