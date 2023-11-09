package cn.stylefeng.roses.kernel.sys.modular.user.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户证书封装类
 *
 * @author fengshuonan
 * @since 2023/11/09 22:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserCertificateRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键")
    private Long userCertificateId;

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空", groups = {add.class, edit.class})
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 证书类型，取字典id
     */
    @NotNull(message = "证书类型，取字典id不能为空", groups = {add.class, edit.class})
    @ChineseDescription("证书类型，取字典id")
    private Long certificateType;

    /**
     * 证书编号
     */
    @ChineseDescription("证书编号")
    private String certificateNo;

    /**
     * 发证机构名称
     */
    @ChineseDescription("发证机构名称")
    private String issuingAuthority;

    /**
     * 证书发证日期
     */
    @ChineseDescription("证书发证日期")
	private String dateIssued;

    /**
     * 证书到期日期，如果为空，则为长期
     */
    @ChineseDescription("证书到期日期，如果为空，则为长期")
	private String dateExpires;

    /**
     * 是否删除：Y-删除，N-未删除
     */
    @NotBlank(message = "是否删除：Y-删除，N-未删除不能为空", groups = {add.class, edit.class})
    @ChineseDescription("是否删除：Y-删除，N-未删除")
    private String delFlag;


    /**
     * 批量删除用的id集合
     */
    @NotNull(message = "批量删除id集合不能为空", groups = batchDelete.class)
    @ChineseDescription("批量删除用的id集合")
    private List<Long> batchDeleteIdList;

}
