package cn.stylefeng.roses.kernel.sys.modular.role.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseExpandFieldEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 系统角色实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@TableName(value = "sys_role", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseExpandFieldEntity {

    /**
     * 主键id
     */
    @TableId(value = "role_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键id")
    private Long roleId;

    /**
     * 角色名称
     */
    @TableField("role_name")
    @ChineseDescription("角色名称")
    private String roleName;

    /**
     * 角色编码
     */
    @TableField("role_code")
    @ChineseDescription("角色编码")
    private String roleCode;

    /**
     * 序号
     */
    @TableField("role_sort")
    @ChineseDescription("序号")
    private BigDecimal roleSort;

    /**
     * 数据范围类型：10-仅本人数据，20-本部门数据，30-本部门及以下数据，40-指定部门数据，50-全部数据
     */
    @TableField("data_scope_type")
    @ChineseDescription("数据范围类型：10-仅本人数据，20-本部门数据，30-本部门及以下数据，40-指定部门数据，50-全部数据")
    private Integer dataScopeType;

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
     * 是否是系统角色：Y-是，N-否。系统角色不能删除
     */
    @TableField("role_system_flag")
    @ChineseDescription("是否是系统角色：Y-是，N-否。系统角色不能删除")
    private String roleSystemFlag;

}
