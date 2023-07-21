package cn.stylefeng.roses.kernel.log.business.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务日志记录实例类
 *
 * @author fengshuonan
 * @date 2023/07/21 15:00
 */
@TableName("sys_log_business")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysLogBusiness extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "business_log_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long businessLogId;

    /**
     * 日志的业务分类的编码
     */
    @TableField("log_type_code")
    @ChineseDescription("日志的业务分类的编码")
    private String logTypeCode;

    /**
     * 日志的标题，摘要信息
     */
    @TableField("log_title")
    @ChineseDescription("日志的标题，摘要信息")
    private String logTitle;

    /**
     * 日志记录的内容
     */
    @TableField("log_content")
    @ChineseDescription("日志记录的内容")
    private String logContent;

    /**
     * 调用链唯一id，一个id代表一次接口调用
     */
    @TableField("trace_id")
    @ChineseDescription("调用链唯一id，一个id代表一次接口调用")
    private Long traceId;

    /**
     * 当前用户请求的url
     */
    @TableField("request_url")
    @ChineseDescription("当前用户请求的url")
    private String requestUrl;

    /**
     * 请求http方法
     */
    @TableField("http_method")
    @ChineseDescription("请求http方法")
    private String httpMethod;

    /**
     * 客户端的ip
     */
    @TableField("client_ip")
    @ChineseDescription("客户端的ip")
    private String clientIp;

    /**
     * 业务操作的用户id
     */
    @TableField("user_id")
    @ChineseDescription("业务操作的用户id")
    private Long userId;

}
