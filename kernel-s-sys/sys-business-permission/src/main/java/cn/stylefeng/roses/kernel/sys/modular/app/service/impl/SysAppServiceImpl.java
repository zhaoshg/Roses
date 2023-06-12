package cn.stylefeng.roses.kernel.sys.modular.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
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

        // 应用编码不允许修改
        if (!sysApp.getAppCode().equals(sysAppRequest.getAppCode())) {
            throw new ServiceException(SysAppExceptionEnum.APP_CODE_CANT_EDIT);
        }

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

        // 只查询有用的列
        wrapper.select(SysApp::getAppId, SysApp::getAppName, SysApp::getAppCode, SysApp::getAppIcon, SysApp::getStatusFlag, SysApp::getAppSort, BaseEntity::getCreateTime);

        Page<SysApp> sysAppPage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysAppPage);
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

        // 根据搜索条件查询
        String searchText = sysAppRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.like(SysApp::getAppCode, searchText);
            queryWrapper.or().like(SysApp::getAppName, searchText);
            queryWrapper.or().like(SysApp::getRemark, searchText);
        }

        // 根据排序查询
        queryWrapper.orderByAsc(SysApp::getAppSort);

        return queryWrapper;
    }

}