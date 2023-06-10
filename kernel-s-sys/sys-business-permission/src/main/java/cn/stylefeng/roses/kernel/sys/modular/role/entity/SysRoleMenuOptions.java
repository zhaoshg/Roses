package cn.stylefeng.roses.kernel.sys.modular.role.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色和菜单下的功能关联实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@TableName("sys_role_menu_options")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleMenuOptions extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "role_menu_option_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long roleMenuOptionId;

    /**
     * 角色id
     */
    @TableField("role_id")
    @ChineseDescription("角色id")
    private Long roleId;

    /**
     * 菜单功能id，关联sys_menu_options主键id
     */
    @TableField("menu_option_id")
    @ChineseDescription("菜单功能id，关联sys_menu_options主键id")
    private Long menuOptionId;

    /**
     * 租户号
     */
    @TableField("tenant_id")
    @ChineseDescription("租户号")
    private Long tenantId;

}
