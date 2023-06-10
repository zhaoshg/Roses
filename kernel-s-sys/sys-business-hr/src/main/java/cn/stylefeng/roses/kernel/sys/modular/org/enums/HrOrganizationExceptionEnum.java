package cn.stylefeng.roses.kernel.sys.modular.org.enums;

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
public enum HrOrganizationExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    HR_ORGANIZATION_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE +  "10001", "查询结果不存在");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    HrOrganizationExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}