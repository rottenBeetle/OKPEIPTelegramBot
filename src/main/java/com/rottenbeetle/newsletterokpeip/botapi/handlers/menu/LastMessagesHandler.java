package com.rottenbeetle.newsletterokpeip.botapi.handlers.menu;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.InputMessageHandler;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.askgroup.UserProfileData;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.model.UserSubscription;
import com.rottenbeetle.newsletterokpeip.service.MessageService;
import com.rottenbeetle.newsletterokpeip.service.ReplyMessageService;
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
public class LastMessagesHandler implements InputMessageHandler {
    private final MessageService messageService;
    private final UserDataCache userDataCache;
    private final ReplyMessageService replyMessageService;
    private final UserSubscriptionService userSubscriptionService;
    @Value("#{'${telegrambot.botToken}'}")
    private String token;

    public LastMessagesHandler(MessageService messageService, UserDataCache userDataCache, ReplyMessageService replyMessageService, UserSubscriptionService userSubscriptionService) {
        this.messageService = messageService;
        this.userDataCache = userDataCache;
        this.replyMessageService = replyMessageService;
        this.userSubscriptionService = userSubscriptionService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUserCurrentBotState(message.getFrom().getId()).equals(BotState.LAST_MESSAGES)) {
            userDataCache.setUserCurrentBotState(message.getFrom().getId(), BotState.ASK_COUNT_MESSAGES);
        }
        return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String message = inputMsg.getText();
        long userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        BotState botState = userDataCache.getUserCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_COUNT_MESSAGES)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, "reply.lastMessages.askCount");
            userDataCache.setUserCurrentBotState(userId, BotState.SEND_LAST_MESSAGES);
        }

        if (botState.equals(BotState.SEND_LAST_MESSAGES)) {
            List<com.rottenbeetle.newsletterokpeip.model.Message> messages;
            UserSubscription user = userSubscriptionService.getUsersSubscriptionById(userId);
            if (isNumeric(message)){
                messages = messageService.findLastMessages(Integer.parseInt(message),user.getGroupName());

                if (!messages.isEmpty()) {
                    for (com.rottenbeetle.newsletterokpeip.model.Message msg: messages) {
                        try {
                            URL url = new URL("https://api.telegram.org/bot" + token
                                    + "/sendMessage" + "?chat_id=" + chatId + "&text=" + msg.getMessage());
                            HttpURLConnection http = (HttpURLConnection) url.openConnection();
                            int statusCode = http.getResponseCode();
                            System.out.println(statusCode);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else {
                    replyToUser = replyMessageService.getWarningReplyMessage(chatId, "reply.lastMessages.messagesNotFound");
                }

            }else {
                replyToUser = replyMessageService.getWarningReplyMessage(chatId, "reply.lastMessages.mustBeNumber");
                userDataCache.setUserCurrentBotState(userId, BotState.LAST_MESSAGES);
            }

            userDataCache.setUserCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }
        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.LAST_MESSAGES;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
