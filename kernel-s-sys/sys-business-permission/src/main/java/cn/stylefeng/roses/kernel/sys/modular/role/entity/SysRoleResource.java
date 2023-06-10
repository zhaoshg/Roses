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
 * 角色资源关联实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@TableName("sys_role_resource")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRoleResource extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "role_resource_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long roleResourceId;

    /**
     * 角色id
     */
    @TableField("role_id")
    @ChineseDescription("角色id")
    private Long roleId;

    /**
     * 资源编码
     */
    @TableField("resource_code")
    @ChineseDescription("资源编码")
    private String resourceCode;

    /**
     * 资源的业务类型：1-业务类，2-系统类
     */
    @TableField("resource_biz_type")
    @ChineseDescription("资源的业务类型：1-业务类，2-系统类")
    private Integer resourceBizType;

}
