package com.rottenbeetle.newsletterokpeip.botapi.handlers.menu;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.InputMessageHandler;
import com.rottenbeetle.newsletterokpeip.buttons.SubscribeGroupButtons;
import com.rottenbeetle.newsletterokpeip.buttons.UnsubscribeGroupButtons;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.model.UserSubscription;
import com.rottenbeetle.newsletterokpeip.service.ReplyMessageService;
import com.rottenbeetle.newsletterokpeip.service.UserSubscriptionServiceImpl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class CurrentGroupHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final ReplyMessageService messageService;
    private final UserSubscriptionServiceImpl userSubscriptionServiceImpl;
    private final UnsubscribeGroupButtons unsubscribeGroupButtons;
    private final SubscribeGroupButtons subscribeGroupButtons;

    public CurrentGroupHandler(UserDataCache userDataCache, ReplyMessageService messageService, UserSubscriptionServiceImpl userSubscriptionServiceImpl, UnsubscribeGroupButtons unsubscribeGroupButtons, SubscribeGroupButtons subscribeGroupButtons) {
        this.userDataCache = userDataCache;
        this.messageService = messageService;
        this.userSubscriptionServiceImpl = userSubscriptionServiceImpl;
        this.unsubscribeGroupButtons = unsubscribeGroupButtons;
        this.subscribeGroupButtons = subscribeGroupButtons;
    }

    @Override
    public SendMessage handle(Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage sendMessage;
        UserSubscription userSubscription = userSubscriptionServiceImpl.getUsersSubscriptionById(userId);
        if (userSubscription == null || userSubscription.getGroupName() == null){
            sendMessage = messageService.getReplyMessage(chatId, "reply.askGroup");
            sendMessage.setReplyMarkup(subscribeGroupButtons.getInlineMessageButtons());
        }else {
            userDataCache.setUserCurrentBotState(userId,BotState.SUBSCRIBED);
            sendMessage = messageService.getReplyMessage(chatId, "reply.yourGroup", userSubscription.getGroupName());
            sendMessage.setReplyMarkup(unsubscribeGroupButtons.getInlineMessageButton(userId));
        }

        return sendMessage;

    }

    @Override
    public BotState getHandlerName() {
        return BotState.CURRENT_GROUP;
    }
}
