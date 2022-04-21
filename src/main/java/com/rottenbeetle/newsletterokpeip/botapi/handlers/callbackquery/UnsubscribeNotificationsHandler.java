package com.rottenbeetle.newsletterokpeip.botapi.handlers.callbackquery;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.NewsletterTelegramBot;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.model.UserSubscription;
import com.rottenbeetle.newsletterokpeip.service.ParseQueryDataService;
import com.rottenbeetle.newsletterokpeip.service.ReplyMessageService;
import com.rottenbeetle.newsletterokpeip.service.UserSubscriptionServiceImpl;
import com.rottenbeetle.newsletterokpeip.utils.Emojis;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

/**
 * Обрабатывает запрос "Отписаться" от группу
 */
@Component
public class UnsubscribeNotificationsHandler implements CallbackQueryHandler {
    private static final CallbackQueryType HANDLER_QUERY_TYPE = CallbackQueryType.UNSUBSCRIBE;
    private final UserSubscriptionServiceImpl subscriptionService;
    private final ParseQueryDataService parseService;
    private final ReplyMessageService messagesService;
    private final NewsletterTelegramBot telegramBot;
    private final UserDataCache userDataCache;

    public UnsubscribeNotificationsHandler(UserSubscriptionServiceImpl subscriptionService,
                                           ParseQueryDataService parseService,
                                           ReplyMessageService messagesService,
                                           @Lazy NewsletterTelegramBot telegramBot, UserDataCache userDataCache) {
        this.subscriptionService = subscriptionService;
        this.parseService = parseService;
        this.messagesService = messagesService;
        this.telegramBot = telegramBot;
        this.userDataCache = userDataCache;
    }

    @Override
    public CallbackQueryType getHandlerQueryType() {
        return HANDLER_QUERY_TYPE;
    }

    @Override
    public SendMessage handleCallbackQuery(CallbackQuery callbackQuery) {
        final long chatId = callbackQuery.getMessage().getChatId();
        final long userId = callbackQuery.getFrom().getId();

        final Long subscriptionID = Long.valueOf(parseService.parseSubscriptionIdFromUnsubscribeQuery(callbackQuery));
       UserSubscription userSubscription = subscriptionService.getUsersSubscriptionById(subscriptionID);
        if (userSubscription == null) {
            return messagesService.getWarningReplyMessage(chatId, "reply.query.userHasNoSubscription");
        }

        subscriptionService.deleteUserSubscription(subscriptionID);

        telegramBot.sendChangedInlineButtonText(callbackQuery,
                String.format("%s %s", Emojis.SUCCESS_UNSUBSCRIBED, UserChatButtonStatus.UNSUBSCRIBED),
                CallbackQueryType.QUERY_PROCESSED.name());

        userDataCache.setUserCurrentBotState(userId, BotState.ASK_GROUP);

        return messagesService.getSuccessReplyMessage(chatId, "reply.query.unsubscribed");
    }
}
