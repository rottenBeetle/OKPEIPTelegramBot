package com.rottenbeetle.newsletterokpeip.botapi.handlers.menu;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.InputMessageHandler;
import com.rottenbeetle.newsletterokpeip.buttons.SubscribeGroupButtons;
import com.rottenbeetle.newsletterokpeip.buttons.UnsubscribeGroupButtons;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.model.UserSubscription;
import com.rottenbeetle.newsletterokpeip.service.MainMenuService;
import com.rottenbeetle.newsletterokpeip.service.ReplyMessageService;
import com.rottenbeetle.newsletterokpeip.service.ScheduleService;
import com.rottenbeetle.newsletterokpeip.service.UserSubscriptionServiceImpl;
import com.rottenbeetle.newsletterokpeip.utils.Emojis;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/*
   Выводит текущую группу на которую подписан пользователь.
 */
@Component
public class CurrentGroupHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final ReplyMessageService messageService;
    private final UserSubscriptionServiceImpl userSubscriptionServiceImpl;
    private final UnsubscribeGroupButtons unsubscribeGroupButtons;
    private final SubscribeGroupButtons subscribeGroupButtons;
    private final ScheduleService scheduleService;
    private final MainMenuService mainMenuService;

    public CurrentGroupHandler(UserDataCache userDataCache, ReplyMessageService messageService, UserSubscriptionServiceImpl userSubscriptionServiceImpl, UnsubscribeGroupButtons unsubscribeGroupButtons, SubscribeGroupButtons subscribeGroupButtons, ScheduleService scheduleService, MainMenuService mainMenuService) {
        this.userDataCache = userDataCache;
        this.messageService = messageService;
        this.userSubscriptionServiceImpl = userSubscriptionServiceImpl;
        this.unsubscribeGroupButtons = unsubscribeGroupButtons;
        this.subscribeGroupButtons = subscribeGroupButtons;
        this.scheduleService = scheduleService;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public SendMessage handle(Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage sendMessage;
        UserSubscription userSubscription = userSubscriptionServiceImpl.getUsersSubscriptionById(userId);
        if (userSubscription == null || userSubscription.getGroupName() == null){
            if (scheduleService.findAllGroupName().isEmpty()){
                return mainMenuService.getMainMenuMessage(chatId,messageService.getEmojiReplyText("reply.query.groupsNotFound", Emojis.NOTIFICATION_MARK_FAILED));
            }else {
                sendMessage = messageService.getReplyMessage(chatId, "reply.askGroup");
                sendMessage.setReplyMarkup(subscribeGroupButtons.getInlineMessageButtons());
            }
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
