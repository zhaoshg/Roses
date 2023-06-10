package cn.stylefeng.roses.kernel.sys.modular.org.entity;

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
 * 组织机构信息实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
@TableName("hr_organization")
@Data
@EqualsAndHashCode(callSuper = true)
public class HrOrganization extends BaseEntity {

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
    @TableField("org_short_name")
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
    @TableField("status_flag")
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
    @TableField("remark")
    @ChineseDescription("描述")
    private String remark;

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
