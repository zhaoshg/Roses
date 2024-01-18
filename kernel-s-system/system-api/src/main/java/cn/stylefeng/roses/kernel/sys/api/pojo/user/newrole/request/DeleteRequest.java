package cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 新的授权界面，点击单个删除按钮
 *
 * @author fengshuonan
 * @since 2024/1/17 21:25
 */
@Data
public class DeleteRequest {

    /**
     * 用户id
     */
    @NotNull(message = "所操作的机构id不能为空，请检查orgId参数")
    private Long userId;

    /**
     * 所操作的机构id
     */
    @NotNull(message = "所操作的机构id不能为空，请检查orgId参数")
    private Long orgId;

}