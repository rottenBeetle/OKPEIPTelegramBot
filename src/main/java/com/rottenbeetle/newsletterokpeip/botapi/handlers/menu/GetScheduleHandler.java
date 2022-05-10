package com.rottenbeetle.newsletterokpeip.botapi.handlers.menu;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.InputMessageHandler;
import com.rottenbeetle.newsletterokpeip.buttons.SubscribeGroupButtons;
import com.rottenbeetle.newsletterokpeip.model.Schedule;
import com.rottenbeetle.newsletterokpeip.model.UserSubscription;
import com.rottenbeetle.newsletterokpeip.service.ReplyMessageService;
import com.rottenbeetle.newsletterokpeip.service.ScheduleServiceImpl;
import com.rottenbeetle.newsletterokpeip.service.UserSubscriptionServiceImpl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

/*
   Получает расписание на всю неделю для текущей группы пользователя.
 */
@Component
public class GetScheduleHandler implements InputMessageHandler {

    private final ScheduleServiceImpl scheduleServiceImpl;
    private final SubscribeGroupButtons subscribeGroupButtons;
    private final UserSubscriptionServiceImpl userSubscriptionServiceImpl;
    private final ReplyMessageService replyMessageService;

    public GetScheduleHandler(ScheduleServiceImpl scheduleServiceImpl, SubscribeGroupButtons subscribeGroupButtons, UserSubscriptionServiceImpl userSubscriptionServiceImpl, ReplyMessageService replyMessageService) {
        this.scheduleServiceImpl = scheduleServiceImpl;
        this.subscribeGroupButtons = subscribeGroupButtons;
        this.userSubscriptionServiceImpl = userSubscriptionServiceImpl;
        this.replyMessageService = replyMessageService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.GET_SCHEDULE;
    }

    private SendMessage processUsersInput(Message inputMessage) {
        //FIXME Выводить отдельными сообщениями
        long userId = inputMessage.getFrom().getId();
        long chatId = inputMessage.getChatId();
        String message = "";
        UserSubscription userSubscription = userSubscriptionServiceImpl.getUsersSubscriptionById(userId);
        if (userSubscription == null || userSubscription.getGroupName() == null) {
            SendMessage replyToUser = null;
            replyToUser = replyMessageService.getReplyMessage(chatId, "reply.askGroup");
            replyToUser.setReplyMarkup(subscribeGroupButtons.getInlineMessageButtons());
            return replyToUser;
        } else {

            List<Schedule> scheduleList = scheduleServiceImpl.getAllScheduleForGroup(userSubscription.getGroupName());

            for (Schedule schedule : scheduleList) {
                if (schedule.getWeekDay() == null || schedule.getLessons() == null) {
                    return replyMessageService.getWarningReplyMessage(chatId, "reply.scheduleNotFill");
                }else {
                    message += "----" + schedule.getWeekDay() + "----" + "\n";
                    for (int i = 0; i < schedule.getLessons().length; i++) {
                        message += i + 1 + ". " + schedule.getLessons()[i] + "\n";
                    }
                }
            }
        }
        return new SendMessage(String.valueOf(chatId), message);
    }

}
