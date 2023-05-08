package cn.stylefeng.roses.kernel.system.modular.user.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 用户组织机构修改的封装
 *
 * @author fengshuonan
 * @since 2023/4/17 18:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserOrgRequest extends BaseRequest {

    /**
     * 更新用户的组织机构id
     */
    @ChineseDescription("更新用户的组织机构id")
    @NotNull(message = "组织机构id不能为空", groups = edit.class)
    private Long orgId;

}
