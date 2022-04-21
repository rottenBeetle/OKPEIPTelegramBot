package com.rottenbeetle.newsletterokpeip.botapi.handlers.askgroup;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.InputMessageHandler;
import com.rottenbeetle.newsletterokpeip.buttons.SubscribeGroupButtons;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.model.UserSubscription;
import com.rottenbeetle.newsletterokpeip.service.MainMenuService;
import com.rottenbeetle.newsletterokpeip.service.ReplyMessageService;
import com.rottenbeetle.newsletterokpeip.service.UserSubscriptionServiceImpl;
import com.rottenbeetle.newsletterokpeip.utils.Emojis;
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
    private final ReplyMessageService replyMessageService;
    private final SubscribeGroupButtons subscribeGroupButtons;
    private final MainMenuService mainMenuService;
    private final UserSubscriptionServiceImpl userSubscriptionServiceImpl;
    private final UserDataCache userDataCache;


    public AskGroupHandler(ReplyMessageService replyMessageService, SubscribeGroupButtons subscribeGroupButtons, MainMenuService mainMenuService, UserSubscriptionServiceImpl userSubscriptionServiceImpl, UserDataCache userDataCache) {

        this.replyMessageService = replyMessageService;
        this.subscribeGroupButtons = subscribeGroupButtons;
        this.mainMenuService = mainMenuService;
        this.userSubscriptionServiceImpl = userSubscriptionServiceImpl;
        this.userDataCache = userDataCache;
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
        SendMessage replyToUser = null;

        UserSubscription userSubscription = userSubscriptionServiceImpl.getUsersSubscriptionById(userId);
        if (userSubscription == null || userSubscription.getGroupName() == null){
            replyToUser = replyMessageService.getReplyMessage(chatId, "reply.askGroup");
            replyToUser.setReplyMarkup(subscribeGroupButtons.getInlineMessageButtons());
        }else {
            userDataCache.setUserCurrentBotState(userId,BotState.SUBSCRIBED);
            return mainMenuService.getMainMenuMessage(chatId,replyMessageService.getEmojiReplyText("reply.mainMenu.welcomeMessage", Emojis.HELP_MENU_WELCOME));
        }

        return replyToUser;
    }


}
