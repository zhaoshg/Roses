package cn.stylefeng.roses.kernel.sys.modular.message.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.sys.api.enums.message.ReadFlagEnum;
import cn.stylefeng.roses.kernel.sys.api.pojo.message.MessageSendDTO;
import cn.stylefeng.roses.kernel.sys.modular.message.entity.SysMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 创建我的消息
 *
 * @author fengshuonan
 * @since 2024/1/14 22:24
 */
public class MessageFactory {

    /**
     * 批量创建个人消息
     *
     * @author fengshuonan
     * @since 2024/1/14 22:24
     */
    public static List<SysMessage> createBatchMessage(MessageSendDTO messageSendDTO) {

        List<SysMessage> sysMessages = new ArrayList<>();

        if (ObjectUtil.isEmpty(messageSendDTO)) {
            return sysMessages;
        }

        Set<Long> userIdList = messageSendDTO.getUserIdList();
        if (ObjectUtil.isEmpty(userIdList)) {
            return sysMessages;
        }

        for (Long userId : userIdList) {
            SysMessage sysMessage = new SysMessage();
            sysMessage.setSendUserId(messageSendDTO.getSendUserId());
            sysMessage.setReceiveUserId(userId);
            sysMessage.setMessageTitle(messageSendDTO.getMessageTitle());
            sysMessage.setMessageContent(messageSendDTO.getMessageContent());
            sysMessage.setMessageType(messageSendDTO.getMessageType());
            sysMessage.setMessageUrl(messageSendDTO.getMessageUrl());
            sysMessage.setPriorityLevel(messageSendDTO.getPriorityLevel());
            sysMessage.setBusinessId(messageSendDTO.getBusinessId());
            sysMessage.setBusinessType(messageSendDTO.getBusinessType());
            sysMessage.setReadFlag(ReadFlagEnum.NO_READ.getCode());
            sysMessages.add(sysMessage);
        }

        return sysMessages;
    }

}
