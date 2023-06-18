package cn.stylefeng.roses.kernel.sys.modular.login.service;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.sys.api.SysUserOrgServiceApi;
import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.SimpleUserDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.UserOrgDTO;
import cn.stylefeng.roses.kernel.sys.modular.login.pojo.IndexUserOrgInfo;
import cn.stylefeng.roses.kernel.sys.modular.login.pojo.UserIndexInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取用户首页信息的业务
 *
 * @author fengshuonan
 * @since 2023/6/18 22:55
 */
@Service
public class UserIndexInfoService {

    @Resource
    private SysUserServiceApi sysUserServiceApi;

    @Resource
    private SysUserOrgServiceApi sysUserOrgServiceApi;

    /**
     * 获取用户首页信息
     *
     * @author fengshuonan
     * @since 2023/6/18 22:55
     */
    public UserIndexInfo getUserIndexInfo() {

        // 返回结果初始化
        UserIndexInfo userIndexInfo = new UserIndexInfo();

        // 获取当前登录用户
        LoginUser loginUser = LoginContext.me().getLoginUser();

        // 1. 获取用户的姓名和头像
        this.fillUserBaseInfo(loginUser.getUserId(), userIndexInfo);

        // 2. 获取用户所有的部门和任职信息，并默认激活（选中）主部门的任职信息
        this.fillUserOrgInfo(loginUser, userIndexInfo);

        // 3. 获取用户的权限编码集合

        // 4. 获取用户的当前登录App和菜单

        // 5. 获取菜单和路由的appId映射关系

        // 6. 构建websocket url

        return userIndexInfo;
    }

    /**
     * 填充用户的基本姓名和头像信息
     *
     * @author fengshuonan
     * @since 2023/6/18 23:01
     */
    private void fillUserBaseInfo(Long userId, UserIndexInfo userIndexInfo) {
        SimpleUserDTO simpleUserDTO = sysUserServiceApi.getUserInfoByUserId(userId);
        userIndexInfo.setUserId(simpleUserDTO.getUserId());
        userIndexInfo.setRealName(simpleUserDTO.getRealName());
        userIndexInfo.setAvatarUrl(simpleUserDTO.getAvatarUrl());
    }

    /**
     * 获取用户的任职信息，包含了机构的详细描述
     * <p>
     * 如果指定用户没有当前激活的组织机构id，则直接指定默认的组织机构
     * <p>
     * 用在首页获取用户详情接口
     *
     * @author fengshuonan
     * @since 2023/6/18 23:33
     */
    private void fillUserOrgInfo(LoginUser loginUser, UserIndexInfo userIndexInfo) {

        List<IndexUserOrgInfo> resultUserOrg = new ArrayList<>();

        // 获取当前用户是否有已经激活的公司id
        Long currentOrgId = loginUser.getCurrentOrgId();

        // 获取用户的所有组织机构集合
        List<UserOrgDTO> userOrgList = sysUserOrgServiceApi.getUserOrgList(loginUser.getUserId());

        // 查询到机构为空，直接返回
        if (ObjectUtil.isEmpty(userOrgList)) {
            userIndexInfo.setUserOrgInfo(resultUserOrg);
            return;
        }

        // 将查询结果转化为前端需要的信息
        for (UserOrgDTO userOrgItem : userOrgList) {
            IndexUserOrgInfo indexUserOrgInfo = new IndexUserOrgInfo();
            indexUserOrgInfo.setUserId(loginUser.getUserId());

            // 设置当前orgId为部门id，如果部门id为空，则代表当前用户只绑定到公司id了
            if (ObjectUtil.isNotEmpty(userOrgItem.getDeptId())) {
                indexUserOrgInfo.setOrgId(userOrgItem.getDeptId());
            } else {
                indexUserOrgInfo.setOrgId(userOrgItem.getCompanyId());
            }

            indexUserOrgInfo.setCompanyName(userOrgItem.getCompanyName());
            indexUserOrgInfo.setDeptName(userOrgItem.getDeptName());
            indexUserOrgInfo.setPositionName(userOrgItem.getPositionName());

            // 设置是否是主要任职部门
            indexUserOrgInfo.setMainFlag(YesOrNotEnum.Y.getCode().equals(userOrgItem.getMainFlag()));

            resultUserOrg.add(indexUserOrgInfo);
        }

        // 设置用户的选中的组织机构
        // 如果当前没激活的组织机构，则直接将主部门激活
        if (currentOrgId == null) {
            for (IndexUserOrgInfo indexUserOrgInfo : resultUserOrg) {
                if (indexUserOrgInfo.getMainFlag()) {
                    indexUserOrgInfo.setCurrentSelectFlag(true);
                } else {
                    indexUserOrgInfo.setCurrentSelectFlag(false);
                }
            }
        }
        // 如果有激活的组织机构
        else {
            for (IndexUserOrgInfo indexUserOrgInfo : resultUserOrg) {
                if (indexUserOrgInfo.getOrgId().equals(currentOrgId)) {
                    indexUserOrgInfo.setCurrentSelectFlag(true);
                } else {
                    indexUserOrgInfo.setCurrentSelectFlag(false);
                }
            }
        }

        // 填充用户组织机构信息
        userIndexInfo.setUserOrgInfo(resultUserOrg);
    }

}
