package cn.stylefeng.roses.kernel.sys.modular.position.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 职位信息实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:25
 */
@TableName("hr_position")
@Data
@EqualsAndHashCode(callSuper = true)
public class HrPosition extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "position_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long positionId;

    /**
     * 职位名称
     */
    @TableField("position_name")
    @ChineseDescription("职位名称")
    private String positionName;

    /**
     * 职位编码
     */
    @TableField("position_code")
    @ChineseDescription("职位编码")
    private String positionCode;

    /**
     * 排序
     */
    @TableField("position_sort")
    @ChineseDescription("排序")
    private BigDecimal positionSort;

    /**
     * 状态：1-启用，2-禁用
     */
    @TableField("status_flag")
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 备注
     */
    @TableField("remark")
    @ChineseDescription("备注")
    private String remark;

    /**
     * 拓展字段
     */
    @TableField("expand_field")
    @ChineseDescription("拓展字段")
    private String expandField;

    /**
     * 乐观锁
     */
    @TableField("version_flag")
    @ChineseDescription("乐观锁")
    private Long versionFlag;

    /**
     * 删除标记：Y-已删除，N-未删除
     */
    @TableField("del_flag")
    @ChineseDescription("删除标记：Y-已删除，N-未删除")
    private String delFlag;

    /**
     * 租户号
     */
    @TableField("tenant_id")
    @ChineseDescription("租户号")
    private Long tenantId;

}
