package cn.stylefeng.roses.kernel.sys.modular.message.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
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

/**
 * 系统消息业务实现层
 *
 * @author fengshuonan
 * @since 2024/01/12 17:31
 */
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage> implements SysMessageService {

    @Override
    public void del(SysMessageRequest sysMessageRequest) {
        SysMessage sysMessage = this.querySysMessage(sysMessageRequest);
        this.removeById(sysMessage.getMessageId());
    }

    @Override
    public SysMessage detail(SysMessageRequest sysMessageRequest) {
        return this.querySysMessage(sysMessageRequest);
    }

    @Override
    public PageResult<SysMessage> findPage(SysMessageRequest sysMessageRequest) {
        LambdaQueryWrapper<SysMessage> wrapper = createWrapper(sysMessageRequest);

        // 查询关键字段
        wrapper.select(SysMessage::getMessageId, SysMessage::getMessageTitle, SysMessage::getPriorityLevel, SysMessage::getMessageSendTime);

        Page<SysMessage> pageList = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(pageList);
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

        // 根据标题查询
        String searchText = sysMessageRequest.getSearchText();
        if (StrUtil.isNotBlank(searchText)) {
            queryWrapper.like(SysMessage::getMessageTitle, searchText);
        }

        // 根据优先级查询
        String priorityLevel = sysMessageRequest.getPriorityLevel();
        if (ObjectUtil.isNotEmpty(priorityLevel)) {
            queryWrapper.eq(SysMessage::getPriorityLevel, priorityLevel);
        }

        // 根据已读状态
        Integer readFlag = sysMessageRequest.getReadFlag();
        if (ObjectUtil.isNotEmpty(readFlag)) {
            queryWrapper.eq(SysMessage::getReadFlag, readFlag);
        }

        return queryWrapper;
    }

}
