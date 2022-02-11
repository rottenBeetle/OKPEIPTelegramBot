package com.rottenbeetle.newsletterokpeip.botapi.handlers.askgroup;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.InputMessageHandler;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


/**
 * Спрашивает пользователя его группу.
 */
@Component
@Slf4j
public class AskGroupHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessageService replyMessageService;

    public AskGroupHandler(UserDataCache userDataCache, ReplyMessageService replyMessageService) {
        this.userDataCache = userDataCache;
        this.replyMessageService = replyMessageService;
    }


    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_GROUP;
    }

    private SendMessage processUsersInput(Message inputMessage) {
        long userId = inputMessage.getFrom().getId();
        long chatId = inputMessage.getChatId();

        SendMessage replyToUser = replyMessageService.getReplyMessage(chatId,"reply.askGroup");
        userDataCache.setUserCurrentBotState(userId,BotState.FILLING_PROFILE);
        return replyToUser;
    }


}
