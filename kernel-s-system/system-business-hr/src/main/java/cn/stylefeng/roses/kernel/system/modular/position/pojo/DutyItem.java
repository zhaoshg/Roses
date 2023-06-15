package cn.stylefeng.roses.kernel.system.modular.position.pojo;

import lombok.Data;

/**
 * 第三方系统的职务信息描述
 *
 * @author fengshuonan
 * @since 2023/6/15 16:57
 */
@Data
public class DutyItem {

    /**
     * 第三方的用户id
     */
    private String userId;

    /**
     * 第三方的组织机构id
     */
    private String orgId;

    /**
     * 组织机构名称
     */
    private String orgName;

    /**
     * 职务编码
     */
    private String dutyCode;

    /**
     * 职务名称
     */
    private String dutyName;

}
