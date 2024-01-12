package cn.stylefeng.roses.kernel.sys.modular.notice.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Map;

/**
 * 通知管理实例类
 *
 * @author fengshuonan
 * @since 2024/01/12 16:06
 */
@TableName("sys_notice")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysNotice extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "notice_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long noticeId;

    /**
     * 通知标题
     */
    @TableField("notice_title")
    @ChineseDescription("通知标题")
    private String noticeTitle;

    /**
     * 通知摘要
     */
    @TableField("notice_summary")
    @ChineseDescription("通知摘要")
    private String noticeSummary;

    /**
     * 通知内容
     */
    @TableField("notice_content")
    @ChineseDescription("通知内容")
    private String noticeContent;

    /**
     * 优先级，来自字典：high-高优先级，middle-中，low-低
     */
    @TableField("priority_level")
    @ChineseDescription("优先级，来自字典：high-高优先级，middle-中，low-低")
    private String priorityLevel;

    /**
     * 开始时间
     */
    @TableField("notice_begin_time")
    @ChineseDescription("开始时间")
    private Date noticeBeginTime;

    /**
     * 结束时间
     */
    @TableField("notice_end_time")
    @ChineseDescription("结束时间")
    private Date noticeEndTime;

    /**
     * 通知范围，存用户id集合
     */
    @TableField("notice_user_scope")
    @ChineseDescription("通知范围，存用户id集合")
    private Map<String, Object> noticeUserScope;

    /**
     * 是否发布：1-已发布，2-未发布
     */
    @TableField("publish_status")
    @ChineseDescription("是否发布：1-已发布，2-未发布")
    private Integer publishStatus;

    /**
     * 乐观锁
     */
    @TableField("version_flag")
    @ChineseDescription("乐观锁")
    private Long versionFlag;

    /**
     * 拓展字段
     */
    @TableField("expand_field")
    @ChineseDescription("拓展字段")
    private Map<String, Object> expandField;

    /**
     * 是否删除：Y-被删除，N-未删除
     */
    @TableField("del_flag")
    @ChineseDescription("是否删除：Y-被删除，N-未删除")
    private String delFlag;

    /**
     * 租户号
     */
    @TableField("tenant_id")
    @ChineseDescription("租户号")
    private Long tenantId;

}
