package cn.stylefeng.roses.kernel.sys.modular.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.message.entity.SysMessage;
import cn.stylefeng.roses.kernel.sys.modular.message.enums.SysMessageExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.message.mapper.SysMessageMapper;
import cn.stylefeng.roses.kernel.sys.modular.message.pojo.request.SysMessageRequest;
import cn.stylefeng.roses.kernel.sys.modular.message.service.SysMessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统消息业务实现层
 *
 * @author fengshuonan
 * @since 2024/01/12 17:31
 */
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements SysMessageService {

    @Override
    public void add(SysMessageRequest sysMessageRequest) {
        SysMessage sysMessage = new SysMessage();
        BeanUtil.copyProperties(sysMessageRequest, sysMessage);
        this.save(sysMessage);
    }

    @Override
    public void del(SysMessageRequest sysMessageRequest) {
        SysMessage sysMessage = this.querySysMessage(sysMessageRequest);
        this.removeById(sysMessage.getMessageId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(SysMessageRequest sysMessageRequest) {
        this.removeByIds(sysMessageRequest.getBatchDeleteIdList());
    }

    @Override
    public void edit(SysMessageRequest sysMessageRequest) {
        SysMessage sysMessage = this.querySysMessage(sysMessageRequest);
        BeanUtil.copyProperties(sysMessageRequest, sysMessage);
        this.updateById(sysMessage);
    }

    @Override
    public SysMessage detail(SysMessageRequest sysMessageRequest) {
        return this.querySysMessage(sysMessageRequest);
    }

    @Override
    public PageResult<SysMessage> findPage(SysMessageRequest sysMessageRequest) {
        LambdaQueryWrapper<SysMessage> wrapper = createWrapper(sysMessageRequest);
        Page<SysMessage> pageList = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(pageList);
    }

    @Override
    public List<SysMessage> findList(SysMessageRequest sysMessageRequest) {
        LambdaQueryWrapper<SysMessage> wrapper = this.createWrapper(sysMessageRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    private SysMessage querySysMessage(SysMessageRequest sysMessageRequest) {
        SysMessage sysMessage = this.getById(sysMessageRequest.getMessageId());
        if (ObjectUtil.isEmpty(sysMessage)) {
            throw new ServiceException(SysMessageExceptionEnum.SYS_MESSAGE_NOT_EXISTED);
        }
        return sysMessage;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    private LambdaQueryWrapper<SysMessage> createWrapper(SysMessageRequest sysMessageRequest) {
        LambdaQueryWrapper<SysMessage> queryWrapper = new LambdaQueryWrapper<>();

        Long messageId = sysMessageRequest.getMessageId();
        Long receiveUserId = sysMessageRequest.getReceiveUserId();
        Long sendUserId = sysMessageRequest.getSendUserId();
        String messageTitle = sysMessageRequest.getMessageTitle();
        String messageContent = sysMessageRequest.getMessageContent();
        String messageType = sysMessageRequest.getMessageType();
        String messageUrl = sysMessageRequest.getMessageUrl();
        String priorityLevel = sysMessageRequest.getPriorityLevel();
        String messageSendTime = sysMessageRequest.getMessageSendTime();
        String businessId = sysMessageRequest.getBusinessId();
        String businessType = sysMessageRequest.getBusinessType();
        Integer readFlag = sysMessageRequest.getReadFlag();
        Long versionFlag = sysMessageRequest.getVersionFlag();
        String delFlag = sysMessageRequest.getDelFlag();
        Long tenantId = sysMessageRequest.getTenantId();

        queryWrapper.eq(ObjectUtil.isNotNull(messageId), SysMessage::getMessageId, messageId);
        queryWrapper.eq(ObjectUtil.isNotNull(receiveUserId), SysMessage::getReceiveUserId, receiveUserId);
        queryWrapper.eq(ObjectUtil.isNotNull(sendUserId), SysMessage::getSendUserId, sendUserId);
        queryWrapper.like(ObjectUtil.isNotEmpty(messageTitle), SysMessage::getMessageTitle, messageTitle);
        queryWrapper.like(ObjectUtil.isNotEmpty(messageContent), SysMessage::getMessageContent, messageContent);
        queryWrapper.like(ObjectUtil.isNotEmpty(messageType), SysMessage::getMessageType, messageType);
        queryWrapper.like(ObjectUtil.isNotEmpty(messageUrl), SysMessage::getMessageUrl, messageUrl);
        queryWrapper.like(ObjectUtil.isNotEmpty(priorityLevel), SysMessage::getPriorityLevel, priorityLevel);
        queryWrapper.eq(ObjectUtil.isNotNull(messageSendTime), SysMessage::getMessageSendTime, messageSendTime);
        queryWrapper.like(ObjectUtil.isNotEmpty(businessId), SysMessage::getBusinessId, businessId);
        queryWrapper.like(ObjectUtil.isNotEmpty(businessType), SysMessage::getBusinessType, businessType);
        queryWrapper.eq(ObjectUtil.isNotNull(readFlag), SysMessage::getReadFlag, readFlag);
        queryWrapper.eq(ObjectUtil.isNotNull(versionFlag), SysMessage::getVersionFlag, versionFlag);
        queryWrapper.like(ObjectUtil.isNotEmpty(delFlag), SysMessage::getDelFlag, delFlag);
        queryWrapper.eq(ObjectUtil.isNotNull(tenantId), SysMessage::getTenantId, tenantId);

        return queryWrapper;
    }

}
