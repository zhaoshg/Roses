package cn.stylefeng.roses.kernel.sys.modular.menu.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单下的功能操作实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@TableName("sys_menu_options")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenuOptions extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "menu_option_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long menuOptionId;

    /**
     * 菜单id
     */
    @TableField("menu_id")
    @ChineseDescription("菜单id")
    private Long menuId;

    /**
     * 功能或操作的名称
     */
    @TableField("option_name")
    @ChineseDescription("功能或操作的名称")
    private String optionName;

    /**
     * 功能或操作的编码
     */
    @TableField("option_code")
    @ChineseDescription("功能或操作的编码")
    private String optionCode;

    /**
     * 租户号
     */
    @TableField("tenant_id")
    @ChineseDescription("租户号")
    private Long tenantId;

}
