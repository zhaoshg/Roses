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
 * 菜单资源绑定实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@TableName("sys_menu_resource")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenuResource extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "menu_resource_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long menuResourceId;

    /**
     * 绑定资源的类型：1-菜单，2-菜单下按钮
     */
    @TableField("business_type")
    @ChineseDescription("绑定资源的类型：1-菜单，2-菜单下按钮")
    private Integer businessType;

    /**
     * 菜单或按钮id
     */
    @TableField("business_id")
    @ChineseDescription("菜单或按钮id")
    private Long businessId;

    /**
     * 资源的编码
     */
    @TableField("resource_code")
    @ChineseDescription("资源的编码")
    private String resourceCode;

    /**
     * 租户号
     */
    @TableField("tenant_id")
    @ChineseDescription("租户号")
    private Long tenantId;

}
