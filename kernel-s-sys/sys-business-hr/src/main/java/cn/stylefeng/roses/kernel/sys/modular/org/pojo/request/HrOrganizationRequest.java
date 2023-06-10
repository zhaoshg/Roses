package cn.stylefeng.roses.kernel.sys.modular.org.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 组织机构信息封装类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HrOrganizationRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键")
    private Long orgId;

    /**
     * 父id，一级节点父id是0
     */
    @NotNull(message = "父id，一级节点父id是0不能为空", groups = {add.class, edit.class})
    @ChineseDescription("父id，一级节点父id是0")
    private Long orgParentId;

    /**
     * 父ids
     */
    @NotBlank(message = "父ids不能为空", groups = {add.class, edit.class})
    @ChineseDescription("父ids")
    private String orgPids;

    /**
     * 组织名称
     */
    @NotBlank(message = "组织名称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("组织名称")
    private String orgName;

    /**
     * 组织机构简称
     */
    @ChineseDescription("组织机构简称")
    private String orgShortName;

    /**
     * 组织编码
     */
    @NotBlank(message = "组织编码不能为空", groups = {add.class, edit.class})
    @ChineseDescription("组织编码")
    private String orgCode;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {add.class, edit.class})
    @ChineseDescription("排序")
    private BigDecimal orgSort;

    /**
     * 状态：1-启用，2-禁用
     */
    @NotNull(message = "状态：1-启用，2-禁用不能为空", groups = {add.class, edit.class})
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 组织机构类型：1-公司，2-部门
     */
    @ChineseDescription("组织机构类型：1-公司，2-部门")
    private Integer orgType;

    /**
     * 税号
     */
    @ChineseDescription("税号")
    private String taxNo;

    /**
     * 描述
     */
    @ChineseDescription("描述")
    private String remark;

    /**
     * 组织机构层级
     */
    @ChineseDescription("组织机构层级")
    private Integer orgLevel;

    /**
     * 对接外部主数据的机构id
     */
    @ChineseDescription("对接外部主数据的机构id")
    private String masterOrgId;

    /**
     * 对接外部主数据的父级机构id
     */
    @ChineseDescription("对接外部主数据的父级机构id")
    private String masterOrgParentId;

    /**
     * 拓展字段
     */
    @ChineseDescription("拓展字段")
    private String expandField;

    /**
     * 乐观锁
     */
    @ChineseDescription("乐观锁")
    private Long versionFlag;

    /**
     * 删除标记：Y-已删除，N-未删除
     */
    @NotBlank(message = "删除标记：Y-已删除，N-未删除不能为空", groups = {add.class, edit.class})
    @ChineseDescription("删除标记：Y-已删除，N-未删除")
    private String delFlag;

    /**
     * 租户号
     */
    @ChineseDescription("租户号")
    private Long tenantId;

}
