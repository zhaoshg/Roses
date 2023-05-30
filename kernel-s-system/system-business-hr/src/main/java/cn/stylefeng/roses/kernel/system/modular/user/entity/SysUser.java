/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.stylefeng.roses.kernel.system.modular.user.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseExpandFieldEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 系统用户表
 *
 * @author luojie
 * @since 2020/11/6 09:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user")
public class SysUser extends BaseExpandFieldEntity {

    /**
     * 主键
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;

    /**
     * 姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 账号
     */
    @TableField("account")
    private String account;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 头像
     */
    @TableField("avatar")
    private Long avatar;

    /**
     * 生日
     */
    @TableField("birthday")
    private Date birthday;

    /**
     * 性别(字典 M男 F女 )
     */
    @TableField("sex")
    private String sex;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 手机
     */
    @TableField("phone")
    private String phone;

    /**
     * 电话
     */
    @TableField("tel")
    private String tel;

    /**
     * 是否是超级管理员，超级管理员可以拥有所有权限（Y-是，N-否）
     */
    @TableField("super_admin_flag")
    private String superAdminFlag;

    /**
     * 状态（字典 1正常 2禁用 3冻结）
     */
    @TableField("status_flag")
    private Integer statusFlag;

    /**
     * 登录次数
     */
    @TableField("login_count")
    private Integer loginCount;

    /**
     * 最后登陆IP
     */
    @TableField("last_login_ip")
    private String lastLoginIp;

    /**
     * 最后登陆时间
     */
    @TableField("last_login_time")
    private Date lastLoginTime;

    /**
     * 对接外部主数据的用户id
     */
    @TableField("master_user_id")
    private String masterUserId;

}
