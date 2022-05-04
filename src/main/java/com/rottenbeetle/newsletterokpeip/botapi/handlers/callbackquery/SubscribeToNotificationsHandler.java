package com.rottenbeetle.newsletterokpeip.botapi.handlers.callbackquery;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.NewsletterTelegramBot;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.askgroup.UserProfileData;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.model.UserSubscription;
import com.rottenbeetle.newsletterokpeip.service.*;
import com.rottenbeetle.newsletterokpeip.utils.Emojis;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;


/**
 * Обрабатывает запрос "Подписаться" на группу
 */
@Component
public class SubscribeToNotificationsHandler implements CallbackQueryHandler {
    private static final CallbackQueryType HANDLER_QUERY_TYPE = CallbackQueryType.SUBSCRIBE;
    private final UserDataCache userDataCache;
    private final ParseQueryDataService parseService;
    private final ReplyMessageService messageService;
    private final UserSubscriptionServiceImpl subscriptionService;
    private final ReplyMessageService replyMessageService;
    private final NewsletterTelegramBot telegramBot;
    private final MainMenuService mainMenuService;

    public SubscribeToNotificationsHandler(UserDataCache userDataCache, ParseQueryDataService parseService,
                                           ReplyMessageService messageService, UserSubscriptionServiceImpl subscriptionService, ReplyMessageService replyMessageService,
                                           @Lazy NewsletterTelegramBot telegramBot, MainMenuService mainMenuService) {
        this.userDataCache = userDataCache;
        this.parseService = parseService;
        this.messageService = messageService;
        this.subscriptionService = subscriptionService;
        this.replyMessageService = replyMessageService;
        this.telegramBot = telegramBot;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public CallbackQueryType getHandlerQueryType() {
        return HANDLER_QUERY_TYPE;
    }

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        final long chatId = callbackQuery.getMessage().getChatId();
        final long userId = callbackQuery.getFrom().getId();
        final String groupName = parseService.parseGroupNameFromSubscribeQuery(callbackQuery);

        UserSubscription userSubscription = new UserSubscription(userId,chatId,groupName);

        if (subscriptionService.hasSubscription(userSubscription)) {
            return replyMessageService.getWarningReplyMessage(chatId, "reply.query.userHasSubscription");
        }

        subscriptionService.saveUserSubscription(userSubscription);
        telegramBot.sendChangedInlineButtonText(callbackQuery,
                String.format("%s %s", Emojis.SUCCESS_SUBSCRIBED, UserChatButtonStatus.SUBSCRIBED), CallbackQueryType.QUERY_PROCESSED.name());
        UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
        userProfileData.setGroup(groupName);
        userDataCache.setUserCurrentBotState(userId, BotState.SHOW_MAIN_MENU);

        return mainMenuService.getMainMenuMessage(chatId,messageService.getEmojiReplyText("reply.mainMenu.welcomeMessage", Emojis.HELP_MENU_WELCOME));

    }



//    private Optional<UserSubscription> parseQueryData(CallbackQuery usersQuery) {
//        List<Schedule> foundedSchedule = userDataCache.getSchedule(usersQuery.getMessage().getChatId());
//        final long chatId = usersQuery.getMessage().getChatId();
//
//        final String groupName = parseService.parseGroupNameFromSubscribeQuery(usersQuery);
//
//        Optional<Schedule> queriedScheduleOptional = foundedSchedule.stream().
//                filter(schedule -> schedule.getGroup().equals(groupName)).findFirst();
//
//        if (!queriedScheduleOptional.isPresent()) {
//            return Optional.empty();
//        }
//
//        Schedule queriedSchedule = queriedScheduleOptional.get();
//        final String group = queriedSchedule.getGroup();
//
//
//        return Optional.of(new UserSubscription(chatId, group));
//    }


}
