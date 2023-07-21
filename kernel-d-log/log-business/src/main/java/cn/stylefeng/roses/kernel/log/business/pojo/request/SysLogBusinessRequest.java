package cn.stylefeng.roses.kernel.log.business.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 业务日志记录封装类
 *
 * @author fengshuonan
 * @date 2023/07/21 15:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysLogBusinessRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键")
    private Long businessLogId;

    /**
     * 日志的业务分类的编码
     */
    @ChineseDescription("日志的业务分类的编码")
    private String logTypeCode;

    /**
     * 日志的标题，摘要信息
     */
    @ChineseDescription("日志的标题，摘要信息")
    private String logTitle;

    /**
     * 日志记录的内容
     */
    @ChineseDescription("日志记录的内容")
    private String logContent;

    /**
     * 调用链唯一id，一个id代表一次接口调用
     */
    @ChineseDescription("调用链唯一id，一个id代表一次接口调用")
    private Long traceId;

    /**
     * 当前用户请求的url
     */
    @ChineseDescription("当前用户请求的url")
    private String requestUrl;

    /**
     * 请求http方法
     */
    @ChineseDescription("请求http方法")
    private String httpMethod;

    /**
     * 客户端的ip
     */
    @ChineseDescription("客户端的ip")
    private String clientIp;

    /**
     * 业务操作的用户id
     */
    @ChineseDescription("业务操作的用户id")
    private Long userId;

}
