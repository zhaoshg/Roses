package cn.stylefeng.roses.kernel.sys.modular.notice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.enums.notice.NoticePublishStatusEnum;
import cn.stylefeng.roses.kernel.sys.modular.notice.entity.SysNotice;
import cn.stylefeng.roses.kernel.sys.modular.notice.enums.SysNoticeExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.notice.mapper.SysNoticeMapper;
import cn.stylefeng.roses.kernel.sys.modular.notice.pojo.NoticeUserScope;
import cn.stylefeng.roses.kernel.sys.modular.notice.pojo.request.SysNoticeRequest;
import cn.stylefeng.roses.kernel.sys.modular.notice.service.SysNoticeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 通知管理业务实现层
 *
 * @author fengshuonan
 * @since 2024/01/12 16:06
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysNoticeRequest sysNoticeRequest) {

        // 1. 判断通知人员范围不能为空
        NoticeUserScope noticeUserScope = sysNoticeRequest.getNoticeUserScope();
        if (noticeUserScope == null) {
            throw new ServiceException(SysNoticeExceptionEnum.SYS_NOTICE_SCOPE_EMPTY);
        }

        // 2. 通知的人员和部门不能同时为空
        if (ObjectUtil.isEmpty(noticeUserScope.getPointOrgList()) && ObjectUtil.isEmpty(noticeUserScope.getPointUserList())) {
            throw new ServiceException(SysNoticeExceptionEnum.SYS_NOTICE_SCOPE_EMPTY);
        }

        // 3. 存储基础信息
        SysNotice sysNotice = new SysNotice();
        BeanUtil.copyProperties(sysNoticeRequest, sysNotice);

        // 新增的时候，默认设置状态为未发布
        sysNotice.setPublishStatus(NoticePublishStatusEnum.NOT_PUBLISH.getCode());

        this.save(sysNotice);
    }

    @Override
    public void del(SysNoticeRequest sysNoticeRequest) {
        SysNotice sysNotice = this.querySysNotice(sysNoticeRequest);
        this.removeById(sysNotice.getNoticeId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(SysNoticeRequest sysNoticeRequest) {
        this.removeByIds(sysNoticeRequest.getBatchDeleteIdList());
    }

    @Override
    public void edit(SysNoticeRequest sysNoticeRequest) {
        SysNotice sysNotice = this.querySysNotice(sysNoticeRequest);
        BeanUtil.copyProperties(sysNoticeRequest, sysNotice);
        this.updateById(sysNotice);
    }

    @Override
    public SysNotice detail(SysNoticeRequest sysNoticeRequest) {
        return this.querySysNotice(sysNoticeRequest);
    }

    @Override
    public PageResult<SysNotice> findPage(SysNoticeRequest sysNoticeRequest) {
        LambdaQueryWrapper<SysNotice> wrapper = createWrapper(sysNoticeRequest);
        Page<SysNotice> pageList = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(pageList);
    }

    @Override
    public List<SysNotice> findList(SysNoticeRequest sysNoticeRequest) {
        LambdaQueryWrapper<SysNotice> wrapper = this.createWrapper(sysNoticeRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    private SysNotice querySysNotice(SysNoticeRequest sysNoticeRequest) {
        SysNotice sysNotice = this.getById(sysNoticeRequest.getNoticeId());
        if (ObjectUtil.isEmpty(sysNotice)) {
            throw new ServiceException(SysNoticeExceptionEnum.SYS_NOTICE_NOT_EXISTED);
        }
        return sysNotice;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @since 2024/01/12 16:06
     */
    private LambdaQueryWrapper<SysNotice> createWrapper(SysNoticeRequest sysNoticeRequest) {
        LambdaQueryWrapper<SysNotice> queryWrapper = new LambdaQueryWrapper<>();

        Long noticeId = sysNoticeRequest.getNoticeId();
        String noticeTitle = sysNoticeRequest.getNoticeTitle();
        String noticeSummary = sysNoticeRequest.getNoticeSummary();
        String noticeContent = sysNoticeRequest.getNoticeContent();
        String priorityLevel = sysNoticeRequest.getPriorityLevel();
        String noticeBeginTime = sysNoticeRequest.getNoticeBeginTime();
        String noticeEndTime = sysNoticeRequest.getNoticeEndTime();
        Map<String, Object> noticeUserScope = sysNoticeRequest.getNoticeUserScope();
        Integer publishStatus = sysNoticeRequest.getPublishStatus();
        Long versionFlag = sysNoticeRequest.getVersionFlag();
        Map<String, Object> expandField = sysNoticeRequest.getExpandField();
        String delFlag = sysNoticeRequest.getDelFlag();
        Long tenantId = sysNoticeRequest.getTenantId();

        queryWrapper.eq(ObjectUtil.isNotNull(noticeId), SysNotice::getNoticeId, noticeId);
        queryWrapper.like(ObjectUtil.isNotEmpty(noticeTitle), SysNotice::getNoticeTitle, noticeTitle);
        queryWrapper.like(ObjectUtil.isNotEmpty(noticeSummary), SysNotice::getNoticeSummary, noticeSummary);
        queryWrapper.like(ObjectUtil.isNotEmpty(noticeContent), SysNotice::getNoticeContent, noticeContent);
        queryWrapper.like(ObjectUtil.isNotEmpty(priorityLevel), SysNotice::getPriorityLevel, priorityLevel);
        queryWrapper.eq(ObjectUtil.isNotNull(noticeBeginTime), SysNotice::getNoticeBeginTime, noticeBeginTime);
        queryWrapper.eq(ObjectUtil.isNotNull(noticeEndTime), SysNotice::getNoticeEndTime, noticeEndTime);
        queryWrapper.eq(ObjectUtil.isNotNull(noticeUserScope), SysNotice::getNoticeUserScope, noticeUserScope);
        queryWrapper.eq(ObjectUtil.isNotNull(publishStatus), SysNotice::getPublishStatus, publishStatus);
        queryWrapper.eq(ObjectUtil.isNotNull(versionFlag), SysNotice::getVersionFlag, versionFlag);
        queryWrapper.eq(ObjectUtil.isNotNull(expandField), SysNotice::getExpandField, expandField);
        queryWrapper.like(ObjectUtil.isNotEmpty(delFlag), SysNotice::getDelFlag, delFlag);
        queryWrapper.eq(ObjectUtil.isNotNull(tenantId), SysNotice::getTenantId, tenantId);

        return queryWrapper;
    }

}
