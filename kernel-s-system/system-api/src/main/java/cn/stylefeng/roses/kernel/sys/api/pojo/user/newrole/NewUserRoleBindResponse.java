package cn.stylefeng.roses.kernel.sys.api.pojo.user.newrole;

import lombok.Data;

import java.util.List;

/**
 * 新的授权界面，用的传输结构
 *
 * @author fengshuonan
 * @since 2024/1/17 20:57
 */
@Data
public class NewUserRoleBindResponse {

    /**
     * 用户所属机构id
     */
    private Long orgId;

    /**
     * 是否启用本公司身份
     */
    private Boolean enableThisOrg;

    /**
     * 角色绑定的详情
     */
    private List<NewUserRoleBindItem> roleBindItemList;

}
