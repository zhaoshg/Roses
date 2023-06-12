package cn.stylefeng.roses.kernel.sys.api.exception.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 组织机构信息异常相关枚举
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
@Getter
public enum UserExceptionEnum implements AbstractExceptionEnum {

    /**
     * 请求状态值为空
     */
    REQUEST_USER_STATUS_EMPTY(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "请求状态值为空"),

    /**
     * 请求状值为非正确状态值
     */
    REQUEST_USER_STATUS_ERROR(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10002", "请求状态值不合法，用户状态参数不合法，参数值：{}");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    UserExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}