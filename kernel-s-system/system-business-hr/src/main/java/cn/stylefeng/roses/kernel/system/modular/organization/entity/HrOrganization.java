package cn.stylefeng.roses.kernel.system.modular.organization.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseBusinessEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 组织机构信息实例类
 *
 * @author fengshuonan
 * @date 2023/05/11 10:12
 */
@TableName(value = "hr_organization", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class HrOrganization extends BaseBusinessEntity {

    /**
     * 主键
     */
    @TableId(value = "org_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long orgId;

    /**
     * 父id，一级节点父id是0
     */
    @TableField("org_parent_id")
    @ChineseDescription("父id，一级节点父id是0")
    private Long orgParentId;

    /**
     * 父ids
     */
    @TableField("org_pids")
    @ChineseDescription("父ids")
    private String orgPids;

    /**
     * 组织名称
     */
    @TableField("org_name")
    @ChineseDescription("组织名称")
    private String orgName;

    /**
     * 组织机构简称
     */
    @TableField(value = "org_short_name")
    @ChineseDescription("组织机构简称")
    private String orgShortName;

    /**
     * 组织编码
     */
    @TableField("org_code")
    @ChineseDescription("组织编码")
    private String orgCode;

    /**
     * 排序
     */
    @TableField("org_sort")
    @ChineseDescription("排序")
    private BigDecimal orgSort;

    /**
     * 状态：1-启用，2-禁用
     */
    @TableField(value = "status_flag", fill = FieldFill.INSERT)
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 组织机构类型：1-公司，2-部门
     */
    @TableField("org_type")
    @ChineseDescription("组织机构类型：1-公司，2-部门")
    private Integer orgType;

    /**
     * 税号
     */
    @TableField("tax_no")
    @ChineseDescription("税号")
    private String taxNo;

    /**
     * 描述
     */
    @TableField("org_remark")
    @ChineseDescription("描述")
    private String orgRemark;

    /**
     * 组织机构层级
     */
    @TableField("org_level")
    @ChineseDescription("组织机构层级")
    private Integer orgLevel;

    /**
     * 对接外部主数据的机构id
     */
    @TableField("master_org_id")
    @ChineseDescription("对接外部主数据的机构id")
    private String masterOrgId;

    /**
     * 对接外部主数据的父级机构id
     */
    @TableField("master_org_parent_id")
    @ChineseDescription("对接外部主数据的父级机构id")
    private String masterOrgParentId;

    /**
     * 拓展字段
     */
    @TableField(value = "expand_field", typeHandler = JacksonTypeHandler.class)
    @ChineseDescription("拓展字段")
    private Map<String, Object> expandField;

}
