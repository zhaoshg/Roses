package cn.stylefeng.roses.kernel.log.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.business.entity.SysLogBusiness;
import cn.stylefeng.roses.kernel.log.business.enums.SysLogBusinessExceptionEnum;
import cn.stylefeng.roses.kernel.log.business.mapper.SysLogBusinessMapper;
import cn.stylefeng.roses.kernel.log.business.pojo.request.SysLogBusinessRequest;
import cn.stylefeng.roses.kernel.log.business.service.SysLogBusinessService;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务日志记录业务实现层
 *
 * @author fengshuonan
 * @date 2023/07/21 15:00
 */
@Service
public class SysLogBusinessServiceImpl extends ServiceImpl<SysLogBusinessMapper, SysLogBusiness> implements SysLogBusinessService {

    @Override
    public void add(SysLogBusinessRequest sysLogBusinessRequest) {
        SysLogBusiness sysLogBusiness = new SysLogBusiness();
        BeanUtil.copyProperties(sysLogBusinessRequest, sysLogBusiness);
        this.save(sysLogBusiness);
    }

    @Override
    public void del(SysLogBusinessRequest sysLogBusinessRequest) {
        SysLogBusiness sysLogBusiness = this.querySysLogBusiness(sysLogBusinessRequest);
        this.removeById(sysLogBusiness.getBusinessLogId());
    }

    @Override
    public void edit(SysLogBusinessRequest sysLogBusinessRequest) {
        SysLogBusiness sysLogBusiness = this.querySysLogBusiness(sysLogBusinessRequest);
        BeanUtil.copyProperties(sysLogBusinessRequest, sysLogBusiness);
        this.updateById(sysLogBusiness);
    }

    @Override
    public SysLogBusiness detail(SysLogBusinessRequest sysLogBusinessRequest) {
        return this.querySysLogBusiness(sysLogBusinessRequest);
    }

    @Override
    public PageResult<SysLogBusiness> findPage(SysLogBusinessRequest sysLogBusinessRequest) {
        LambdaQueryWrapper<SysLogBusiness> wrapper = createWrapper(sysLogBusinessRequest);
        Page<SysLogBusiness> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public void saveBatchLogs(SysLogBusiness context, List<String> batchContentList) {

        if (ObjectUtil.isEmpty(batchContentList)) {
            return;
        }

        List<SysLogBusiness> sysLogBusinesses = new ArrayList<>();
        for (String content : batchContentList) {
            SysLogBusiness sysLogBusiness = new SysLogBusiness();
            BeanUtil.copyProperties(context, sysLogBusiness);
            sysLogBusiness.setLogContent(content);
            sysLogBusinesses.add(sysLogBusiness);
        }

        this.getBaseMapper().insertBatchSomeColumn(sysLogBusinesses);
    }

    @Override
    public List<SysLogBusiness> findList(SysLogBusinessRequest sysLogBusinessRequest) {
        LambdaQueryWrapper<SysLogBusiness> wrapper = this.createWrapper(sysLogBusinessRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/07/21 15:00
     */
    private SysLogBusiness querySysLogBusiness(SysLogBusinessRequest sysLogBusinessRequest) {
        SysLogBusiness sysLogBusiness = this.getById(sysLogBusinessRequest.getBusinessLogId());
        if (ObjectUtil.isEmpty(sysLogBusiness)) {
            throw new ServiceException(SysLogBusinessExceptionEnum.SYS_LOG_BUSINESS_NOT_EXISTED);
        }
        return sysLogBusiness;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/07/21 15:00
     */
    private LambdaQueryWrapper<SysLogBusiness> createWrapper(SysLogBusinessRequest sysLogBusinessRequest) {
        LambdaQueryWrapper<SysLogBusiness> queryWrapper = new LambdaQueryWrapper<>();

        // 根据日志类型编码查询
        String logTypeCode = sysLogBusinessRequest.getLogTypeCode();
        queryWrapper.eq(ObjectUtil.isNotEmpty(logTypeCode), SysLogBusiness::getLogTypeCode, logTypeCode);

        // 根据调用链日志信息查询
        Long traceId = sysLogBusinessRequest.getTraceId();
        queryWrapper.eq(ObjectUtil.isNotNull(traceId), SysLogBusiness::getTraceId, traceId);

        // 根据文本检索内容查询
        String searchText = sysLogBusinessRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.nested(wrap -> {
                wrap.like(SysLogBusiness::getLogTitle, searchText).or().like(SysLogBusiness::getLogContent, searchText);
            });
        }

        return queryWrapper;
    }

}