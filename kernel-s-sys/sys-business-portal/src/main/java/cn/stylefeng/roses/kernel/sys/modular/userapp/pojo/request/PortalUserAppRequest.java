package cn.stylefeng.roses.kernel.sys.modular.userapp.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 用户常用功能封装类
 *
 * @author fengshuonan
 * @date 2023/06/26 21:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PortalUserAppRequest extends BaseRequest {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键id")
    private Long appLinkId;

    /**
     * 冗余字段，菜单所属的应用id
     */
    @ChineseDescription("冗余字段，菜单所属的应用id")
    private Long appId;

    /**
     * 关联的菜单id
     */
    @ChineseDescription("关联的菜单id")
    private Long menuId;

    /**
     * 租户id
     */
    @ChineseDescription("租户id")
    private Long tenantId;

}
