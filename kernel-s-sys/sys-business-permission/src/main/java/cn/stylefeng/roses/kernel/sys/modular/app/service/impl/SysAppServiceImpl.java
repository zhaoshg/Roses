package cn.stylefeng.roses.kernel.sys.modular.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.app.entity.SysApp;
import cn.stylefeng.roses.kernel.sys.modular.app.enums.SysAppExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.app.mapper.SysAppMapper;
import cn.stylefeng.roses.kernel.sys.modular.app.pojo.request.SysAppRequest;
import cn.stylefeng.roses.kernel.sys.modular.app.service.SysAppService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 系统应用业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
@Service
public class SysAppServiceImpl extends ServiceImpl<SysAppMapper, SysApp> implements SysAppService {

	@Override
    public void add(SysAppRequest sysAppRequest) {
        SysApp sysApp = new SysApp();
        BeanUtil.copyProperties(sysAppRequest, sysApp);
        this.save(sysApp);
    }

    @Override
    public void del(SysAppRequest sysAppRequest) {
        SysApp sysApp = this.querySysApp(sysAppRequest);
        this.removeById(sysApp.getAppId());
    }

    @Override
    public void edit(SysAppRequest sysAppRequest) {
        SysApp sysApp = this.querySysApp(sysAppRequest);
        BeanUtil.copyProperties(sysAppRequest, sysApp);
        this.updateById(sysApp);
    }

    @Override
    public SysApp detail(SysAppRequest sysAppRequest) {
        return this.querySysApp(sysAppRequest);
    }

    @Override
    public PageResult<SysApp> findPage(SysAppRequest sysAppRequest) {
        LambdaQueryWrapper<SysApp> wrapper = createWrapper(sysAppRequest);
        Page<SysApp> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysApp> findList(SysAppRequest sysAppRequest) {
        LambdaQueryWrapper<SysApp> wrapper = this.createWrapper(sysAppRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    private SysApp querySysApp(SysAppRequest sysAppRequest) {
        SysApp sysApp = this.getById(sysAppRequest.getAppId());
        if (ObjectUtil.isEmpty(sysApp)) {
            throw new ServiceException(SysAppExceptionEnum.SYS_APP_NOT_EXISTED);
        }
        return sysApp;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    private LambdaQueryWrapper<SysApp> createWrapper(SysAppRequest sysAppRequest) {
        LambdaQueryWrapper<SysApp> queryWrapper = new LambdaQueryWrapper<>();

        Long appId = sysAppRequest.getAppId();
        String appName = sysAppRequest.getAppName();
        String appCode = sysAppRequest.getAppCode();
        Long appIcon = sysAppRequest.getAppIcon();
        Integer statusFlag = sysAppRequest.getStatusFlag();
        BigDecimal appSort = sysAppRequest.getAppSort();
        String remark = sysAppRequest.getRemark();
        String expandField = sysAppRequest.getExpandField();
        Long versionFlag = sysAppRequest.getVersionFlag();
        String delFlag = sysAppRequest.getDelFlag();
        Long tenantId = sysAppRequest.getTenantId();

        queryWrapper.eq(ObjectUtil.isNotNull(appId), SysApp::getAppId, appId);
        queryWrapper.like(ObjectUtil.isNotEmpty(appName), SysApp::getAppName, appName);
        queryWrapper.like(ObjectUtil.isNotEmpty(appCode), SysApp::getAppCode, appCode);
        queryWrapper.eq(ObjectUtil.isNotNull(appIcon), SysApp::getAppIcon, appIcon);
        queryWrapper.eq(ObjectUtil.isNotNull(statusFlag), SysApp::getStatusFlag, statusFlag);
        queryWrapper.eq(ObjectUtil.isNotNull(appSort), SysApp::getAppSort, appSort);
        queryWrapper.like(ObjectUtil.isNotEmpty(remark), SysApp::getRemark, remark);
        queryWrapper.like(ObjectUtil.isNotEmpty(expandField), SysApp::getExpandField, expandField);
        queryWrapper.eq(ObjectUtil.isNotNull(versionFlag), SysApp::getVersionFlag, versionFlag);
        queryWrapper.like(ObjectUtil.isNotEmpty(delFlag), SysApp::getDelFlag, delFlag);
        queryWrapper.eq(ObjectUtil.isNotNull(tenantId), SysApp::getTenantId, tenantId);

        return queryWrapper;
    }

}