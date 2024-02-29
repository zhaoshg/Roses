package cn.stylefeng.roses.kernel.db.mp.datascope.config;

import cn.stylefeng.roses.kernel.rule.enums.permission.DataScopeTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * 数据范围权限配置
 *
 * @author fengshuonan
 * @since 2024-02-29 10:04
 */
@Data
public class DataScopeConfig {

    /**
     * 用户的数据范围权限类型
     */
    private DataScopeTypeEnum dataScopeType;

    /**
     * 限制的用户id
     */
    private Long userId;

    /**
     * 用户所在部门id
     */
    private Long userDeptId;

    /**
     * 用户所在公司id
     */
    private Long userCompanyId;

    /**
     * 指定机构的ID列表，仅在数据范围类型为 DEFINE 时使用
     */
    private List<Long> specificOrgIds;

    /**
     * 限制组织机构范围的字段名称
     */
    private String orgIdFieldName = "org_id";

    /**
     * 用来限制只查询自己数据的字段名称
     */
    private String userIdFieldName = "user_id";

}
