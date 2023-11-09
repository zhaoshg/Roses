package cn.stylefeng.roses.kernel.sys.modular.user.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户证书实例类
 *
 * @author fengshuonan
 * @since 2023/11/09 22:44
 */
@TableName("sys_user_certificate")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserCertificate extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "user_certificate_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("主键")
    private Long userCertificateId;

    /**
     * 用户id
     */
    @TableField("user_id")
    @ChineseDescription("用户id")
    private Long userId;

    /**
     * 证书类型，取字典id
     */
    @TableField("certificate_type")
    @ChineseDescription("证书类型，取字典id")
    private Long certificateType;

    /**
     * 证书编号
     */
    @TableField("certificate_no")
    @ChineseDescription("证书编号")
    private String certificateNo;

    /**
     * 发证机构名称
     */
    @TableField("issuing_authority")
    @ChineseDescription("发证机构名称")
    private String issuingAuthority;

    /**
     * 证书发证日期
     */
    @TableField("date_issued")
    @ChineseDescription("证书发证日期")
    private Date dateIssued;

    /**
     * 证书到期日期，如果为空，则为长期
     */
    @TableField("date_expires")
    @ChineseDescription("证书到期日期，如果为空，则为长期")
    private Date dateExpires;

    /**
     * 是否删除：Y-删除，N-未删除
     */
    @TableField("del_flag")
    @ChineseDescription("是否删除：Y-删除，N-未删除")
    private String delFlag;

}
