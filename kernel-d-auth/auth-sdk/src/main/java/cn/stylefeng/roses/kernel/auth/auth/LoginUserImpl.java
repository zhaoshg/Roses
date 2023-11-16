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
package cn.stylefeng.roses.kernel.auth.auth;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.LoginUserApi;
import cn.stylefeng.roses.kernel.auth.api.SessionManagerApi;
import cn.stylefeng.roses.kernel.auth.api.constants.AuthConstants;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.context.LoginUserHolder;
import cn.stylefeng.roses.kernel.auth.api.exception.AuthException;
import cn.stylefeng.roses.kernel.auth.api.exception.enums.AuthExceptionEnum;
import cn.stylefeng.roses.kernel.auth.api.loginuser.CommonLoginUserUtil;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.sys.api.OrganizationServiceApi;
import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.CompanyDeptDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.TempLoginUserInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.function.Consumer;

/**
 * 当前登陆用户的接口实现
 *
 * @author fengshuonan
 * @since 2020/10/21 18:09
 */
@Service
public class LoginUserImpl implements LoginUserApi {

    @Resource
    private SessionManagerApi sessionManagerApi;

    @Resource
    private SysUserServiceApi sysUserServiceApi;

    @Resource
    private OrganizationServiceApi organizationServiceApi;

    @Override
    public String getToken() {
        return CommonLoginUserUtil.getToken();
    }

    @Override
    public LoginUser getLoginUser() throws AuthException {

        // 先从ThreadLocal中获取
        LoginUser currentUser = LoginUserHolder.get();
        if (currentUser != null) {
            return currentUser;
        }

        // 获取用户的token
        String token = getToken();

        // 获取session中该token对应的用户
        LoginUser session = sessionManagerApi.getSession(token);

        // session为空抛出异常
        if (session == null) {
            throw new AuthException(AuthExceptionEnum.AUTH_EXPIRED_ERROR);
        }

        return session;
    }

    @Override
    public LoginUser getLoginUserNullable() {

        // 先从ThreadLocal中获取
        LoginUser currentUser = LoginUserHolder.get();
        if (currentUser != null) {
            return currentUser;
        }

        // 获取用户的token
        String token = null;
        try {
            token = getToken();
        } catch (Exception e) {
            return null;
        }

        // 获取session中该token对应的用户
        return sessionManagerApi.getSession(token);

    }

    @Override
    public boolean hasLogin() {

        // 获取用户的token
        String token = null;
        try {
            token = getToken();
        } catch (Exception e) {
            return false;
        }

        // 获取是否在会话中有
        return sessionManagerApi.haveSession(token);
    }

    @Override
    public boolean getSuperAdminFlag() {
        LoginUser loginUser = getLoginUser();
        return sysUserServiceApi.getUserSuperAdminFlag(loginUser.getUserId());
    }

    @Override
    public Long getCurrentUserCompanyId() {

        Long currentOrgId = LoginContext.me().getLoginUser().getCurrentOrgId();
        if (currentOrgId == null) {
            return null;
        }

        CompanyDeptDTO orgCompanyInfo = organizationServiceApi.getOrgCompanyInfo(currentOrgId);
        if (orgCompanyInfo == null) {
            return null;
        }

        return orgCompanyInfo.getCompanyId();
    }

    @Override
    public LoginUser switchTo(Long userId) {

        if (ObjectUtil.isEmpty(userId)) {
            return null;
        }

        // 获取用户的基本信息
        TempLoginUserInfo tempLoginUserInfo = sysUserServiceApi.createTempUserInfo(userId);
        if (tempLoginUserInfo == null) {
            return null;
        }

        // 创建临时登录用户
        LoginUser loginUser = new LoginUser();
        BeanUtil.copyProperties(tempLoginUserInfo, loginUser, CopyOptions.create().ignoreError());

        // 设置用户的临时token
        loginUser.setToken(AuthConstants.TEMP_TOKEN);

        // 放入线程变量中
        LoginUserHolder.set(loginUser);

        return loginUser;
    }

    @Override
    public void endSwitch() {
        LoginUserHolder.remove();
    }

    @Override
    public void switchTo(Long userId, Consumer<Long> consumer) {
        this.switchTo(userId);
        consumer.accept(userId);
        this.endSwitch();
    }

}
