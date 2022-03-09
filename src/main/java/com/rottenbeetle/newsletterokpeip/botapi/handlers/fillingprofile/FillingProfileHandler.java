/*
package com.rottenbeetle.newsletterokpeip.botapi.handlers.fillingprofile;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.InputMessageHandler;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessageService messageService;

    public FillingProfileHandler(UserDataCache userDataCache, ReplyMessageService messageService) {
        this.userDataCache = userDataCache;
        this.messageService = messageService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUserCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)){
            userDataCache.setUserCurrentBotState(message.getFrom().getId(), BotState.ASK_GROUP);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }



    private SendMessage processUsersInput(Message inputMessage) {
        String userAnswer = inputMessage.getText();
        long userId = inputMessage.getFrom().getId();
        long chatId = inputMessage.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUserCurrentBotState(userId);

        SendMessage replyToUser= null;

        if (botState.equals(BotState.ASK_GROUP)){
            replyToUser = messageService.getReplyMessage(chatId, "reply.askGroup");
            userDataCache.setUserCurrentBotState(userId,BotState.ASK_NAME);
        }
        if (botState.equals(BotState.ASK_NAME)){
            replyToUser = messageService.getReplyMessage(chatId, "reply.askName");
            profileData.setGroup(userAnswer);
            userDataCache.setUserCurrentBotState(userId,BotState.ASK_AGE);
        }
      */
/*  if (botState.equals(BotState.ASK_AGE)){
            replyToUser = messageService.getReplyMessage(chatId, "reply.askAge");
            profileData.setName(userAnswer);
            userDataCache.setUserCurrentBotState(userId,BotState.PROFILE_FILLED);
        }

        if (botState.equals(BotState.PROFILE_FILLED)){
            profileData.setAge(userAnswer);
            replyToUser = new SendMessage(String.valueOf(chatId), String.format("%s %s","Данные по вашей анкете",profileData));
        }*//*

        userDataCache.saveUserProfileData(userId,profileData);
        return replyToUser;
    }


}
*/
