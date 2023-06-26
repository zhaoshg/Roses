package cn.stylefeng.roses.kernel.sys.modular.userapp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.userapp.entity.PortalUserApp;
import cn.stylefeng.roses.kernel.sys.modular.userapp.enums.PortalUserAppExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.userapp.mapper.PortalUserAppMapper;
import cn.stylefeng.roses.kernel.sys.modular.userapp.pojo.request.PortalUserAppRequest;
import cn.stylefeng.roses.kernel.sys.modular.userapp.service.PortalUserAppService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户常用功能业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/26 21:25
 */
@Service
public class PortalUserAppServiceImpl extends ServiceImpl<PortalUserAppMapper, PortalUserApp> implements PortalUserAppService {

	@Override
    public void add(PortalUserAppRequest portalUserAppRequest) {
        PortalUserApp portalUserApp = new PortalUserApp();
        BeanUtil.copyProperties(portalUserAppRequest, portalUserApp);
        this.save(portalUserApp);
    }

    @Override
    public void del(PortalUserAppRequest portalUserAppRequest) {
        PortalUserApp portalUserApp = this.queryPortalUserApp(portalUserAppRequest);
        this.removeById(portalUserApp.getAppLinkId());
    }

    @Override
    public void edit(PortalUserAppRequest portalUserAppRequest) {
        PortalUserApp portalUserApp = this.queryPortalUserApp(portalUserAppRequest);
        BeanUtil.copyProperties(portalUserAppRequest, portalUserApp);
        this.updateById(portalUserApp);
    }

    @Override
    public PortalUserApp detail(PortalUserAppRequest portalUserAppRequest) {
        return this.queryPortalUserApp(portalUserAppRequest);
    }

    @Override
    public PageResult<PortalUserApp> findPage(PortalUserAppRequest portalUserAppRequest) {
        LambdaQueryWrapper<PortalUserApp> wrapper = createWrapper(portalUserAppRequest);
        Page<PortalUserApp> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<PortalUserApp> findList(PortalUserAppRequest portalUserAppRequest) {
        LambdaQueryWrapper<PortalUserApp> wrapper = this.createWrapper(portalUserAppRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    private PortalUserApp queryPortalUserApp(PortalUserAppRequest portalUserAppRequest) {
        PortalUserApp portalUserApp = this.getById(portalUserAppRequest.getAppLinkId());
        if (ObjectUtil.isEmpty(portalUserApp)) {
            throw new ServiceException(PortalUserAppExceptionEnum.PORTAL_USER_APP_NOT_EXISTED);
        }
        return portalUserApp;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    private LambdaQueryWrapper<PortalUserApp> createWrapper(PortalUserAppRequest portalUserAppRequest) {
        LambdaQueryWrapper<PortalUserApp> queryWrapper = new LambdaQueryWrapper<>();

        Long appLinkId = portalUserAppRequest.getAppLinkId();
        Long appId = portalUserAppRequest.getAppId();
        Long menuId = portalUserAppRequest.getMenuId();
        Long tenantId = portalUserAppRequest.getTenantId();

        queryWrapper.eq(ObjectUtil.isNotNull(appLinkId), PortalUserApp::getAppLinkId, appLinkId);
        queryWrapper.eq(ObjectUtil.isNotNull(appId), PortalUserApp::getAppId, appId);
        queryWrapper.eq(ObjectUtil.isNotNull(menuId), PortalUserApp::getMenuId, menuId);
        queryWrapper.eq(ObjectUtil.isNotNull(tenantId), PortalUserApp::getTenantId, tenantId);

        return queryWrapper;
    }

}