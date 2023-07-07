package cn.stylefeng.roses.kernel.sys.api.constants;

/**
 * 权限编码（菜单功能的编码），一般用在给@ApiResource注解加一些权限控制标识
 *
 * @author fengshuonan
 * @since 2023/7/7 14:20
 */
public interface PermissionCodeConstants {

    /**
     * 首页-公司信息统计信息
     */
    String COMPANY_STAT_INFO = "COMPANY_STAT_INFO";

    /**
     * 人员管理界面-新增人员
     */
    String ADD_USER = "ADD_USER";

    /**
     * 人员管理界面-修改人员
     */
    String EDIT_USER = "EDIT_USER";

    /**
     * 人员管理界面-删除人员
     */
    String DELETE_USER = "DELETE_USER";

    /**
     * 人员管理界面-分配角色
     */
    String ASSIGN_USER_ROLE = "ASSIGN_USER_ROLE";

    /**
     * 人员管理界面-重置密码
     */
    String RESET_PASSWORD = "RESET_PASSWORD";

    /**
     * 人员管理界面-修改状态
     */
    String UPDATE_USER_STATUS = "UPDATE_USER_STATUS";

    /**
     * 职位管理界面-新增职位
     */
    String ADD_POSITION = "ADD_POSITION";

    /**
     * 职位管理界面-修改职位
     */
    String EDIT_POSITION = "EDIT_POSITION";

    /**
     * 职位管理界面-删除职位
     */
    String DELETE_POSITION = "DELETE_POSITION";


}
