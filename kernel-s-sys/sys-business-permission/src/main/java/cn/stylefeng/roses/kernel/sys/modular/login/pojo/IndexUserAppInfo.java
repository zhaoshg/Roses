package cn.stylefeng.roses.kernel.sys.modular.login.pojo;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import lombok.Data;

import java.util.List;


/**
 * 用户的应用详情信息，用在首页获取用户信息
 *
 * @author fengshuonan
 * @since 2023/6/19 22:18
 */
@Data
public class IndexUserAppInfo {

    /**
     * 应用id
     */
    @ChineseDescription("应用id")
    private Long appId;

    /**
     * 应用名称
     */
    @ChineseDescription("应用名称")
    private String appName;

    /**
     * 应用菜单集合
     */
    @ChineseDescription("应用菜单集合")
    private List<IndexUserMenuInfo> menuList;

    /**
     * 是否是当前登录用户激活的（选中的）
     */
    @ChineseDescription("是否是当前登录用户激活的（选中的）")
    private Boolean currentSelectFlag;

}
