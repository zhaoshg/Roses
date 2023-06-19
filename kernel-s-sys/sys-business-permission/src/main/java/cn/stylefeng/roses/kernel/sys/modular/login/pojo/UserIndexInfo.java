package cn.stylefeng.roses.kernel.sys.modular.login.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 首页需要的用户信息，包括【个人基本信息】、【菜单信息】、【当前登录的应用】、【当前登录的组织机构身份】
 *
 * @author fengshuonan
 * @since 2023/6/18 22:03
 */
@Data
public class UserIndexInfo {

    /**
     * 用户主键id
     */
    @ChineseDescription("用户主键id")
    private Long userId;

    /**
     * 真实姓名
     */
    @ChineseDescription("真实姓名")
    private String realName;

    /**
     * 头像地址
     */
    @ChineseDescription("头像地址")
    private String avatarUrl;

    /**
     * 当前用户的部门和任职信息
     */
    @ChineseDescription("当前用户的部门和任职信息")
    private List<IndexUserOrgInfo> userOrgInfoList;

    /**
     * 当前用户的权限编码集合，包括【菜单编码】和【菜单功能编码】
     */
    @ChineseDescription("当前用户的权限编码集合，包括【菜单编码】和【菜单功能编码】")
    private Set<String> permissionCodeList;

    /**
     * 当前用户拥有的应用信息
     */
    @ChineseDescription("当前用户拥有的应用信息")
    private List<IndexUserAppInfo> userAppInfoList;

    /**
     * 用户菜单集合
     */
    @ChineseDescription("用户菜单集合")
    private List<IndexUserMenuInfo> menuList;

    /**
     * 菜单路由和appId的映射关系
     * <p>
     * 用在提供前端应用切换的需要
     */
    @ChineseDescription("菜单路由和appId的映射关系")
    private Map<String, Long> menuUrlAppIdMap;

    /**
     * 登录人的websocketUrl，用来获取右上角的实时消息
     */
    @ChineseDescription("登录人的websocketUrl，用来获取右上角的实时消息")
    private String websocketUrl;

}
