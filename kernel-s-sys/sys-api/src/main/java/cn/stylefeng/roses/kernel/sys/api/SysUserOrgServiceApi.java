package cn.stylefeng.roses.kernel.sys.api;

import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserOrgDTO;

import java.util.List;

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

    /**
     * 获取用户绑定的组织机构列表，主要任职部门和次要任职部门都返回
     *
     * @author fengshuonan
     * @since 2023/6/11 21:08
     */
    List<UserOrgDTO> getUserOrgList(Long userId);

    /**
     * 获取某个机构下的所有用户id集合
     *
     * @param orgId             组织机构id
     * @param containSubOrgFlag 是否包含指定机构的子集机构，true-包含子集，false-不包含
     * @author fengshuonan
     * @since 2023/6/11 21:46
     */
    List<Long> getOrgUserIdList(Long orgId, Boolean containSubOrgFlag);

}