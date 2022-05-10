package com.rottenbeetle.newsletterokpeip.botapi.handlers.askgroup;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.InputMessageHandler;
import com.rottenbeetle.newsletterokpeip.buttons.SubscribeGroupButtons;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.model.Role;
import com.rottenbeetle.newsletterokpeip.model.User;
import com.rottenbeetle.newsletterokpeip.model.UserSubscription;
import com.rottenbeetle.newsletterokpeip.service.*;
import com.rottenbeetle.newsletterokpeip.utils.Emojis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Collections;


/**
 * Спрашивает пользователя его группу.
 */
@Component
@Slf4j
public class AskGroupHandler implements InputMessageHandler {
    private final ReplyMessageService replyMessageService;
    private final SubscribeGroupButtons subscribeGroupButtons;
    private final MainMenuService mainMenuService;
    private final UserSubscriptionService userSubscriptionService;
    private final UserService userService;
    private final UserDataCache userDataCache;
    private final ScheduleService scheduleService;
    @Value("#{'${superAdmin}'}")
    private long superAdminId;

    public AskGroupHandler(ReplyMessageService replyMessageService, SubscribeGroupButtons subscribeGroupButtons, MainMenuService mainMenuService, UserSubscriptionService userSubscriptionService, UserService userService, UserDataCache userDataCache, ScheduleService scheduleService) {

        this.replyMessageService = replyMessageService;
        this.subscribeGroupButtons = subscribeGroupButtons;
        this.mainMenuService = mainMenuService;
        this.userSubscriptionService = userSubscriptionService;
        this.userService = userService;
        this.userDataCache = userDataCache;
        this.scheduleService = scheduleService;
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

        Role role;
        if (userId==superAdminId){
            role = Role.ADMIN;
        }else {
            role = Role.USER;
        }

        User user = new User(userId, inputMessage.getChatId(),inputMessage.getFrom().getFirstName(),inputMessage.getFrom().getLastName(),
                inputMessage.getFrom().getUserName(), Collections.singleton(role));
        userService.saveUser(user);

        SendMessage replyToUser = null;

        UserSubscription userSubscription = userSubscriptionService.getUsersSubscriptionById(userId);
        if (userSubscription == null || userSubscription.getGroupName() == null ){
            if (scheduleService.findAllGroupName().isEmpty()){
                return mainMenuService.getMainMenuMessage(chatId,replyMessageService.getEmojiReplyText("reply.query.groupsNotFound", Emojis.NOTIFICATION_MARK_FAILED));
            }else{
                replyToUser = replyMessageService.getReplyMessage(chatId, "reply.askGroup");
                replyToUser.setReplyMarkup(subscribeGroupButtons.getInlineMessageButtons());
            }
        }else {
            userDataCache.setUserCurrentBotState(userId,BotState.SUBSCRIBED);
            return mainMenuService.getMainMenuMessage(chatId,replyMessageService.getEmojiReplyText("reply.mainMenu.welcomeMessage", Emojis.HELP_MENU_WELCOME));
        }

        return replyToUser;
    }


}
