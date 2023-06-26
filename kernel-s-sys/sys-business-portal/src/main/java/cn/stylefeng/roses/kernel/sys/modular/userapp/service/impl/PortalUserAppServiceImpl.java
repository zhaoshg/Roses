package cn.stylefeng.roses.kernel.sys.modular.userapp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.sys.api.SysMenuServiceApi;
import cn.stylefeng.roses.kernel.sys.api.pojo.menu.UserAppMenuInfo;
import cn.stylefeng.roses.kernel.sys.modular.userapp.entity.PortalUserApp;
import cn.stylefeng.roses.kernel.sys.modular.userapp.mapper.PortalUserAppMapper;
import cn.stylefeng.roses.kernel.sys.modular.userapp.pojo.request.PortalUserAppRequest;
import cn.stylefeng.roses.kernel.sys.modular.userapp.service.PortalUserAppService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户常用功能业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/26 21:25
 */
@Service
public class PortalUserAppServiceImpl extends ServiceImpl<PortalUserAppMapper, PortalUserApp> implements PortalUserAppService {

    @Resource
    private SysMenuServiceApi sysMenuServiceApi;

    @Override
    public void updateUserAppList(PortalUserAppRequest portalUserAppRequest) {
        PortalUserApp portalUserApp = new PortalUserApp();
        BeanUtil.copyProperties(portalUserAppRequest, portalUserApp);
        this.save(portalUserApp);
    }

    @Override
    public List<UserAppMenuInfo> getUserAppList() {

        // 获取当前登录用户id
        Long userId = LoginContext.me().getLoginUser().getUserId();

        // 获取所有的用户常用功能（功能其实对应的就是菜单）
        LambdaQueryWrapper<PortalUserApp> portalUserAppLambdaQueryWrapper = new LambdaQueryWrapper<>();
        portalUserAppLambdaQueryWrapper.eq(PortalUserApp::getUserId, userId);
        portalUserAppLambdaQueryWrapper.select(PortalUserApp::getMenuId);
        List<PortalUserApp> userAppList = this.list(portalUserAppLambdaQueryWrapper);

        if (ObjectUtil.isEmpty(userAppList)) {
            return new ArrayList<>();
        }

        // 获取这些功能的菜单id集合
        Set<Long> totalMenuIdList = userAppList.stream().map(PortalUserApp::getMenuId).collect(Collectors.toSet());

        // 通过这些菜单id查询到菜单的名称、图标、路由返回给前端
        return sysMenuServiceApi.getUserAppMenuDetailList(totalMenuIdList);
    }

}