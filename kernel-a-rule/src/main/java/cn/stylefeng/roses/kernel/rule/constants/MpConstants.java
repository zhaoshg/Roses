package cn.stylefeng.roses.kernel.rule.constants;

/**
 * 用于mybatis-plus的typeHandler常量
 *
 * @author fengshuonan
 * @since 2023/8/2 13:55
 */
public class MpConstants {

    /**
     * 一般用在手动使用LambdaUpdateWrapper时候，手动更新json字段，需要手动指定typeHandler
     */
    public static final String TYPE_HANDLER_STRING = "typeHandler=com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler";

}
