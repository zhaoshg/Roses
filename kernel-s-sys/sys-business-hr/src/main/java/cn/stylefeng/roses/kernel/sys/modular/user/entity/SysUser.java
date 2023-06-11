package cn.stylefeng.roses.kernel.sys.modular.user.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseExpandFieldEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 系统用户实例类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@TableName(value = "sys_user", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseExpandFieldEntity {

    /**
     * 主键
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long userId;

    /**
     * 姓名
     */
    @TableField("real_name")
    @ChineseDescription("姓名")
    private String realName;

    /**
     * 昵称
     */
    @TableField("nick_name")
    @ChineseDescription("昵称")
    private String nickName;

    /**
     * 账号
     */
    @TableField("account")
    @ChineseDescription("账号")
    private String account;

    /**
     * 密码，加密方式为BCrypt
     */
    @TableField("password")
    @ChineseDescription("密码，加密方式为BCrypt")
    private String password;

    /**
     * 头像，存的为文件id
     */
    @TableField("avatar")
    @ChineseDescription("头像，存的为文件id")
    private Long avatar;

    /**
     * 生日
     */
    @TableField("birthday")
    @ChineseDescription("生日")
    private Date birthday;

    /**
     * 性别：M-男，F-女
     */
    @TableField("sex")
    @ChineseDescription("性别：M-男，F-女")
    private String sex;

    /**
     * 邮箱
     */
    @TableField("email")
    @ChineseDescription("邮箱")
    private String email;

    /**
     * 手机
     */
    @TableField("phone")
    @ChineseDescription("手机")
    private String phone;

    /**
     * 电话
     */
    @TableField("tel")
    @ChineseDescription("电话")
    private String tel;

    /**
     * 是否是超级管理员：Y-是，N-否
     */
    @TableField("super_admin_flag")
    @ChineseDescription("是否是超级管理员：Y-是，N-否")
    private String superAdminFlag;

    /**
     * 状态：1-正常，2-冻结
     */
    @TableField("status_flag")
    @ChineseDescription("状态：1-正常，2-冻结")
    private Integer statusFlag;

    /**
     * 登录次数
     */
    @TableField("login_count")
    @ChineseDescription("登录次数")
    private Integer loginCount;

    /**
     * 最后登陆IP
     */
    @TableField("last_login_ip")
    @ChineseDescription("最后登陆IP")
    private String lastLoginIp;

    /**
     * 最后登陆时间
     */
    @TableField("last_login_time")
    @ChineseDescription("最后登陆时间")
    private Date lastLoginTime;

    /**
     * 对接外部主数据的用户id
     */
    @TableField("master_user_id")
    @ChineseDescription("对接外部主数据的用户id")
    private String masterUserId;

}
