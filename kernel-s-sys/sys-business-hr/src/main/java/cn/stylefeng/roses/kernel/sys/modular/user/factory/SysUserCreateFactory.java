package cn.stylefeng.roses.kernel.sys.modular.user.factory;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.auth.api.password.PasswordStoredEncryptApi;
import cn.stylefeng.roses.kernel.rule.enums.SexEnum;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUser;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRequest;

/**
 * 用户信息填充，用于创建和修改用户时，添加一些基础信息
 *
 * @author fengshuonan
 * @date 2020/11/21 12:55
 */
public class SysUserCreateFactory {

   
    /**
     * 编辑用户时候的用户信息填充
     *
     * @author fengshuonan
     * @date 2020/11/21 12:56
     */
    public static void fillUpdateInfo(SysUserRequest sysUserRequest, SysUser sysUser) {

        // 性别（M-男，F-女）
        sysUser.setSex(sysUserRequest.getSex());

        // 邮箱
        sysUser.setEmail(sysUserRequest.getEmail());

        // 姓名
        sysUser.setRealName(sysUserRequest.getRealName());

        // 生日
        sysUser.setBirthday(DateUtil.parse(sysUserRequest.getBirthday()));

        // 手机
        sysUser.setPhone(sysUserRequest.getPhone());
    }


}
