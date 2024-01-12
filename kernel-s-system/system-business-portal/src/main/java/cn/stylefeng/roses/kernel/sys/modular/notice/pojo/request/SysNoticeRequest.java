package cn.stylefeng.roses.kernel.sys.modular.notice.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 通知管理封装类
 *
 * @author fengshuonan
 * @since 2024/01/12 16:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysNoticeRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键")
    private Long noticeId;

    /**
     * 通知标题
     */
    @NotBlank(message = "通知标题不能为空", groups = {add.class, edit.class})
    @ChineseDescription("通知标题")
    private String noticeTitle;

    /**
     * 通知摘要
     */
    @ChineseDescription("通知摘要")
    private String noticeSummary;

    /**
     * 通知内容
     */
    @ChineseDescription("通知内容")
    private String noticeContent;

    /**
     * 优先级，来自字典：high-高优先级，middle-中，low-低
     */
    @NotBlank(message = "优先级，来自字典：high-高优先级，middle-中，low-低不能为空", groups = {add.class, edit.class})
    @ChineseDescription("优先级，来自字典：high-高优先级，middle-中，low-低")
    private String priorityLevel;

    /**
     * 开始时间
     */
    @ChineseDescription("开始时间")
	private String noticeBeginTime;

    /**
     * 结束时间
     */
    @ChineseDescription("结束时间")
	private String noticeEndTime;

    /**
     * 通知范围，存用户id集合
     */
    @ChineseDescription("通知范围，存用户id集合")
    private Map<String, Object> noticeUserScope;

    /**
     * 是否发布：1-已发布，2-未发布
     */
    @ChineseDescription("是否发布：1-已发布，2-未发布")
    private Integer publishStatus;

    /**
     * 乐观锁
     */
    @ChineseDescription("乐观锁")
    private Long versionFlag;

    /**
     * 拓展字段
     */
    @ChineseDescription("拓展字段")
    private Map<String, Object> expandField;

    /**
     * 是否删除：Y-被删除，N-未删除
     */
    @NotBlank(message = "是否删除：Y-被删除，N-未删除不能为空", groups = {add.class, edit.class})
    @ChineseDescription("是否删除：Y-被删除，N-未删除")
    private String delFlag;

    /**
     * 租户号
     */
    @ChineseDescription("租户号")
    private Long tenantId;


    /**
     * 批量删除用的id集合
     */
    @NotNull(message = "批量删除id集合不能为空", groups = batchDelete.class)
    @ChineseDescription("批量删除用的id集合")
    private List<Long> batchDeleteIdList;

}
