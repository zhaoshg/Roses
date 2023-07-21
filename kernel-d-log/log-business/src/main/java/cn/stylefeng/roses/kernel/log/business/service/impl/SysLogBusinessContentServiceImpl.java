package cn.stylefeng.roses.kernel.log.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.business.entity.SysLogBusinessContent;
import cn.stylefeng.roses.kernel.log.business.enums.SysLogBusinessContentExceptionEnum;
import cn.stylefeng.roses.kernel.log.business.mapper.SysLogBusinessContentMapper;
import cn.stylefeng.roses.kernel.log.business.pojo.request.SysLogBusinessContentRequest;
import cn.stylefeng.roses.kernel.log.business.service.SysLogBusinessContentService;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务日志记录业务实现层
 *
 * @author fengshuonan
 * @date 2023/07/21 19:02
 */
@Service
public class SysLogBusinessContentServiceImpl extends ServiceImpl<SysLogBusinessContentMapper, SysLogBusinessContent> implements
        SysLogBusinessContentService {

    @Override
    public void add(SysLogBusinessContentRequest sysLogBusinessContentRequest) {
        SysLogBusinessContent sysLogBusinessContent = new SysLogBusinessContent();
        BeanUtil.copyProperties(sysLogBusinessContentRequest, sysLogBusinessContent);
        this.save(sysLogBusinessContent);
    }

    @Override
    public void del(SysLogBusinessContentRequest sysLogBusinessContentRequest) {
        SysLogBusinessContent sysLogBusinessContent = this.querySysLogBusinessContent(sysLogBusinessContentRequest);
        this.removeById(sysLogBusinessContent.getContentId());
    }

    @Override
    public void edit(SysLogBusinessContentRequest sysLogBusinessContentRequest) {
        SysLogBusinessContent sysLogBusinessContent = this.querySysLogBusinessContent(sysLogBusinessContentRequest);
        BeanUtil.copyProperties(sysLogBusinessContentRequest, sysLogBusinessContent);
        this.updateById(sysLogBusinessContent);
    }

    @Override
    public SysLogBusinessContent detail(SysLogBusinessContentRequest sysLogBusinessContentRequest) {
        return this.querySysLogBusinessContent(sysLogBusinessContentRequest);
    }

    @Override
    public PageResult<SysLogBusinessContent> findPage(SysLogBusinessContentRequest sysLogBusinessContentRequest) {
        LambdaQueryWrapper<SysLogBusinessContent> wrapper = createWrapper(sysLogBusinessContentRequest);
        Page<SysLogBusinessContent> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public void batchSaveContent(List<SysLogBusinessContent> sysLogBusinessContentList) {
        this.getBaseMapper().insertBatchSomeColumn(sysLogBusinessContentList);
    }

    @Override
    public List<SysLogBusinessContent> findList(SysLogBusinessContentRequest sysLogBusinessContentRequest) {
        LambdaQueryWrapper<SysLogBusinessContent> wrapper = this.createWrapper(sysLogBusinessContentRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    private SysLogBusinessContent querySysLogBusinessContent(SysLogBusinessContentRequest sysLogBusinessContentRequest) {
        SysLogBusinessContent sysLogBusinessContent = this.getById(sysLogBusinessContentRequest.getContentId());
        if (ObjectUtil.isEmpty(sysLogBusinessContent)) {
            throw new ServiceException(SysLogBusinessContentExceptionEnum.SYS_LOG_BUSINESS_CONTENT_NOT_EXISTED);
        }
        return sysLogBusinessContent;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    private LambdaQueryWrapper<SysLogBusinessContent> createWrapper(SysLogBusinessContentRequest sysLogBusinessContentRequest) {
        LambdaQueryWrapper<SysLogBusinessContent> queryWrapper = new LambdaQueryWrapper<>();

        Long contentId = sysLogBusinessContentRequest.getContentId();
        Long businessLogId = sysLogBusinessContentRequest.getBusinessLogId();
        String logContent = sysLogBusinessContentRequest.getLogContent();

        queryWrapper.eq(ObjectUtil.isNotNull(contentId), SysLogBusinessContent::getContentId, contentId);
        queryWrapper.eq(ObjectUtil.isNotNull(businessLogId), SysLogBusinessContent::getBusinessLogId, businessLogId);
        queryWrapper.like(ObjectUtil.isNotEmpty(logContent), SysLogBusinessContent::getLogContent, logContent);

        return queryWrapper;
    }

}