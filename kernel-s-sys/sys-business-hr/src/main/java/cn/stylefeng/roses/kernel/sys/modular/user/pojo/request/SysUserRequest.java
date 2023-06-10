package cn.stylefeng.roses.kernel.sys.modular.user.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 系统用户封装类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("主键")
    private Long userId;

    /**
     * 姓名
     */
    @ChineseDescription("姓名")
    private String realName;

    /**
     * 昵称
     */
    @ChineseDescription("昵称")
    private String nickName;

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空", groups = {add.class, edit.class})
    @ChineseDescription("账号")
    private String account;

    /**
     * 密码，加密方式为BCrypt
     */
    @NotBlank(message = "密码，加密方式为BCrypt不能为空", groups = {add.class, edit.class})
    @ChineseDescription("密码，加密方式为BCrypt")
    private String password;

    /**
     * 头像，存的为文件id
     */
    @ChineseDescription("头像，存的为文件id")
    private Long avatar;

    /**
     * 生日
     */
    @ChineseDescription("生日")
	private String birthday;

    /**
     * 性别：M-男，F-女
     */
    @NotBlank(message = "性别：M-男，F-女不能为空", groups = {add.class, edit.class})
    @ChineseDescription("性别：M-男，F-女")
    private String sex;

    /**
     * 邮箱
     */
    @ChineseDescription("邮箱")
    private String email;

    /**
     * 手机
     */
    @ChineseDescription("手机")
    private String phone;

    /**
     * 电话
     */
    @ChineseDescription("电话")
    private String tel;

    /**
     * 是否是超级管理员：Y-是，N-否
     */
    @NotBlank(message = "是否是超级管理员：Y-是，N-否不能为空", groups = {add.class, edit.class})
    @ChineseDescription("是否是超级管理员：Y-是，N-否")
    private String superAdminFlag;

    /**
     * 状态：1-正常，2-冻结
     */
    @NotNull(message = "状态：1-正常，2-冻结不能为空", groups = {add.class, edit.class})
    @ChineseDescription("状态：1-正常，2-冻结")
    private Integer statusFlag;

    /**
     * 登录次数
     */
    @ChineseDescription("登录次数")
    private Integer loginCount;

    /**
     * 最后登陆IP
     */
    @ChineseDescription("最后登陆IP")
    private String lastLoginIp;

    /**
     * 最后登陆时间
     */
    @ChineseDescription("最后登陆时间")
	private String lastLoginTime;

    /**
     * 对接外部主数据的用户id
     */
    @ChineseDescription("对接外部主数据的用户id")
    private String masterUserId;

    /**
     * 拓展字段
     */
    @ChineseDescription("拓展字段")
    private String expandField;

    /**
     * 乐观锁
     */
    @ChineseDescription("乐观锁")
    private Long versionFlag;

    /**
     * 删除标记：Y-已删除，N-未删除
     */
    @NotBlank(message = "删除标记：Y-已删除，N-未删除不能为空", groups = {add.class, edit.class})
    @ChineseDescription("删除标记：Y-已删除，N-未删除")
    private String delFlag;

    /**
     * 租户号
     */
    @ChineseDescription("租户号")
    private Long tenantId;

}
