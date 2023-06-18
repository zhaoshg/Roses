package cn.stylefeng.roses.kernel.sys.api;

import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserOrgDTO;

/**
 * 单独编写用户和组织机构关系的Api
 *
 * @author fengshuonan
 * @since 2023/6/18 23:14
 */
public interface SysUserOrgServiceApi {

    /**
     * 获取用户的主要任职信息
     * <p>
     * 返回一条结果，只返回主部门的信息
     *
     * @author fengshuonan
     * @since 2023/6/11 21:07
     */
    UserOrgDTO getUserMainOrgInfo(Long userId);

}
