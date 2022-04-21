package com.rottenbeetle.newsletterokpeip.service;

import com.rottenbeetle.newsletterokpeip.utils.Emojis;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Формирует готовые ответные сообщения в чат
 */
@Service
public class ReplyMessageService {
    private final LocaleMessageService localeMessageService;

    public ReplyMessageService(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    public SendMessage getReplyMessage(long chatId,String replyMessage){
        return new SendMessage(String.valueOf(chatId),localeMessageService.getMessage(replyMessage));
    }

    public SendMessage getReplyMessage(long chatId,String replyMessage,Object... args){
        return new SendMessage(String.valueOf(chatId),localeMessageService.getMessage(replyMessage,args));
    }

    public SendMessage getSuccessReplyMessage(long chatId, String replyMessage) {
        return new SendMessage(Long.toString(chatId), getEmojiReplyText(replyMessage, Emojis.SUCCESS_MARK));
    }

    public SendMessage getSuccessReplyMessage(long chatId, String replyMessage,Object... args) {
        return new SendMessage(Long.toString(chatId), getEmojiReplyText(replyMessage, Emojis.SUCCESS_MARK,args));
    }

    public SendMessage getWarningReplyMessage(long chatId, String replyMessage) {
        return new SendMessage(Long.toString(chatId), getEmojiReplyText(replyMessage, Emojis.NOTIFICATION_MARK_FAILED));
    }

    public String getReplyText(String replyText) {
        return localeMessageService.getMessage(replyText);
    }

    public String getReplyText(String replyText, Object... args) {
        return localeMessageService.getMessage(replyText, args);
    }

    public String getEmojiReplyText(String replyText, Emojis emoji, Object... args) {
        return localeMessageService.getMessage(replyText, emoji,args);
    }

    public String getEmojiReplyText(String replyText, Emojis emoji) {
        return localeMessageService.getMessage(replyText, emoji);
    }

}
