package com.rottenbeetle.newsletterokpeip.botapi.handlers.menu;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.InputMessageHandler;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.askgroup.UserProfileData;
import com.rottenbeetle.newsletterokpeip.buttons.SendMessageButtons;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.model.UserSubscription;
import com.rottenbeetle.newsletterokpeip.service.MessageService;
import com.rottenbeetle.newsletterokpeip.service.ReplyMessageService;
import com.rottenbeetle.newsletterokpeip.service.ScheduleService;
import com.rottenbeetle.newsletterokpeip.service.UserSubscriptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Component
public class SendMessageHandler implements InputMessageHandler {
    private final ReplyMessageService replyMessageService;
    private final UserDataCache userDataCache;
    private final MessageService messageService;
    private final UserSubscriptionService userSubscriptionService;
    private final ScheduleService scheduleService;
    @Value("#{'${telegrambot.botToken}'}")
    private String token;

    public SendMessageHandler(ReplyMessageService replyMessageService, MessageService messageService, UserDataCache userDataCache, UserSubscriptionService userSubscriptionService, ScheduleService scheduleService) {
        this.replyMessageService = replyMessageService;
        this.messageService = messageService;
        this.userDataCache = userDataCache;
        this.userSubscriptionService = userSubscriptionService;
        this.scheduleService = scheduleService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUserCurrentBotState(message.getFrom().getId()).equals(BotState.SENDING_MESSAGE)) {
            userDataCache.setUserCurrentBotState(message.getFrom().getId(), BotState.ASK_GROUP_FOR_MESSAGE);
        }
        return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String message = inputMsg.getText();
        long userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUserCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_GROUP_FOR_MESSAGE)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, "reply.sendMessage.askGroupForMessage");
            userDataCache.setUserCurrentBotState(userId, BotState.ASK_MESSAGE);
        }

        if (botState.equals(BotState.ASK_MESSAGE)) {
            List<String> groups = scheduleService.findAllGroupName();
            message = message.toUpperCase();
            if (!(groups.contains(message) || message.equals("ВСЕМ"))){
                replyToUser = replyMessageService.getWarningReplyMessage(chatId, "reply.sendMessage.notFoundGroup");
                userDataCache.setUserCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
            }else {
                profileData.setGroup(message);
                userDataCache.saveUserProfileData(userId,profileData);

                replyToUser = replyMessageService.getReplyMessage(chatId, "reply.sendMessage.askMessage");
                userDataCache.setUserCurrentBotState(userId, BotState.SEND_MESSAGE);
            }
        }

        if (botState.equals(BotState.SEND_MESSAGE)) {
            profileData.setMessage(message);
            userDataCache.saveUserProfileData(userId,profileData);
            messageService.saveMessage(new com.rottenbeetle.newsletterokpeip.model.Message(profileData.getGroup(), profileData.getMessage()));
            String group = profileData.getGroup();
            List<UserSubscription> userSubscriptions;

            if (group.equals("ВСЕМ")){
                userSubscriptions = userSubscriptionService.getAllSubscription();
            }else{
                 userSubscriptions = userSubscriptionService.findAllByGroupName(group);
            }


            if (!userSubscriptions.isEmpty()) {
                for (UserSubscription user : userSubscriptions) {
                    try {
                        URL url = new URL("https://api.telegram.org/bot" + token
                                + "/sendMessage" + "?chat_id=" + user.getChatId() + "&text=" + message);
                        HttpURLConnection http = (HttpURLConnection) url.openConnection();
                        int statusCode = http.getResponseCode();
                        System.out.println(statusCode);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                replyToUser = replyMessageService.getSuccessReplyMessage(chatId, "reply.query.messagesSuccessSend");
            } else {
                replyToUser = replyMessageService.getWarningReplyMessage(chatId, "reply.query.messagesNotSend");
            }

            userDataCache.setUserCurrentBotState(userId, BotState.SHOW_MAIN_MENU);

        }

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SENDING_MESSAGE;
    }
}
