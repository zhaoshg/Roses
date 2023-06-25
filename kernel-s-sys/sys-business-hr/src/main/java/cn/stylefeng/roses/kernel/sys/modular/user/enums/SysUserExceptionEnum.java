package cn.stylefeng.roses.kernel.sys.modular.user.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 系统用户异常相关枚举
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@Getter
public enum SysUserExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    SYS_USER_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10001", "查询结果不存在"),

    /**
     * 不能删除超级管理员
     */
    USER_CAN_NOT_DELETE_ADMIN(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10002", "不能删除超级管理员"),

    /**
     * 用户账号不存在
     * <p>
     * 但是提示：用户账号或密码错误，请重新输入
     */
    ACCOUNT_NOT_EXIST(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10003", "用户账号或密码错误，请重新输入"),
    
    /**
     * 原密码错误
     */
    USER_PWD_ERROR(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10004", "原密码错误，请重新输入"),

    /**
     * 新密码与原密码相同
     */
    USER_PWD_REPEAT(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + "10005", "新密码与原密码相同，请更换新密码");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    SysUserExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}