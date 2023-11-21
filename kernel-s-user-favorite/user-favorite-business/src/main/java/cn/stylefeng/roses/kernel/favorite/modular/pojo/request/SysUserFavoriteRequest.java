package cn.stylefeng.roses.kernel.favorite.modular.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户收藏信息封装类
 *
 * @author fengshuonan
 * @since 2023/11/21 22:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserFavoriteRequest extends BaseRequest {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键id")
    private Long favoriteId;

    /**
     * 收藏业务的类型，存业务编码
     */
    @NotBlank(message = "收藏业务的类型，存业务编码不能为空", groups = {add.class, edit.class})
    @ChineseDescription("收藏业务的类型，存业务编码")
    private String favType;

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空", groups = {add.class, edit.class})
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 具体业务id
     */
    @NotNull(message = "具体业务id不能为空", groups = {add.class, edit.class})
    @ChineseDescription("具体业务id")
    private Long businessId;

    /**
     * 收藏时间
     */
    @ChineseDescription("收藏时间")
	private String favTime;


    /**
     * 批量删除用的id集合
     */
    @NotNull(message = "批量删除id集合不能为空", groups = batchDelete.class)
    @ChineseDescription("批量删除用的id集合")
    private List<Long> batchDeleteIdList;

}
