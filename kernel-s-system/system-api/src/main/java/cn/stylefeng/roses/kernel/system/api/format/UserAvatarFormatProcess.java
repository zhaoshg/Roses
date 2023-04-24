package cn.stylefeng.roses.kernel.system.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.system.api.UserServiceApi;
import cn.stylefeng.roses.kernel.system.api.pojo.user.SysUserDTO;

import java.util.HashMap;

/**
 * 针对用户详情的渲染，增加对用户头像的支持
 *
 * @author fengshuonan
 * @since 2023/4/24 23:53
 */
public class UserAvatarFormatProcess extends UserFormatProcess {

    protected Object execute(Object businessId) {

        if (ObjectUtil.isEmpty(businessId)) {
            return null;
        }

        Long userId = Convert.toLong(businessId);
        UserServiceApi bean = SpringUtil.getBean(UserServiceApi.class);
        SysUserDTO userInfoByUserId = bean.getUserInfoByUserId(userId);
        if (userInfoByUserId == null) {
            return null;
        }

        // 获取用户姓名
        String realName = userInfoByUserId.getRealName();

        // 获取用户头像
        String avatarUrl = userInfoByUserId.getAvatarUrl();

        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("realName", realName);
        userInfo.put("avatarUrl", avatarUrl);

        return userInfo;
    }

}
