package com.rottenbeetle.newsletterokpeip.botapi.handlers.menu;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.InputMessageHandler;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.askgroup.UserProfileData;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.model.Schedule;
import com.rottenbeetle.newsletterokpeip.model.UserSubscription;
import com.rottenbeetle.newsletterokpeip.service.MessageService;
import com.rottenbeetle.newsletterokpeip.service.ReplyMessageService;
import com.rottenbeetle.newsletterokpeip.service.ScheduleService;
import com.rottenbeetle.newsletterokpeip.service.UserSubscriptionService;
import com.rottenbeetle.newsletterokpeip.utils.Emojis;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/*
   Выполняет CRUD операции над расписаниями и группами.
 */
@Component
public class EditGroupHandler implements InputMessageHandler {
    private final ReplyMessageService replyMessageService;
    private final UserDataCache userDataCache;
    private final ScheduleService scheduleService;
    private String token;
    private static int countFillLessonsForWeekDay;
    private HashMap<Integer, String> weekdays;

    public EditGroupHandler(ReplyMessageService replyMessageService, UserDataCache userDataCache, ScheduleService scheduleService) {
        this.replyMessageService = replyMessageService;
        this.userDataCache = userDataCache;
        this.scheduleService = scheduleService;
        this.weekdays = new HashMap<>();
        weekdays.put(0, "\uD83D\uDE16Понедельник\uD83D\uDE16");
        weekdays.put(1, "\uD83D\uDE0FВторник\uD83D\uDE0F");
        weekdays.put(2, "\uD83D\uDE0CСреда\uD83D\uDE0C");
        weekdays.put(3, "\uD83D\uDE0AЧетверг\uD83D\uDE0A");
        weekdays.put(4, "\uD83D\uDE1CПятница\uD83D\uDE1C");
        weekdays.put(5, "\uD83D\uDE1DСуббота\uD83D\uDE1D");
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUserCurrentBotState(message.getFrom().getId()).equals(BotState.GROUP_HANDLER)) {
            userDataCache.setUserCurrentBotState(message.getFrom().getId(), BotState.ADD_GROUP);
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

        if (botState.equals(BotState.ADD_GROUP)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, "reply.askGroupToAdd");
            userDataCache.setUserCurrentBotState(userId, BotState.SAVE_GROUP);
        }

        if (botState.equals(BotState.SAVE_GROUP)) {
            Set<String> groups = scheduleService.findAllGroupName();
            message = message.toUpperCase();
            profileData.setGroup(message);
            userDataCache.saveUserProfileData(userId, profileData);

            if (!groups.contains(message)) {
                replyToUser = replyMessageService.getReplyMessage(chatId, "reply.askFillingSchedule");
                userDataCache.setUserCurrentBotState(userId, BotState.ASK_FILLING_SCHEDULE);
            } else {
                replyToUser = replyMessageService.getSuccessReplyMessage(chatId, "reply.groupFoundAskAction");
                userDataCache.setUserCurrentBotState(userId, BotState.ACTIONS_GROUP);
            }
        }

        if (botState.equals(BotState.ASK_FILLING_SCHEDULE)) {
            message = message.toUpperCase();
            switch (message) {
                case "ДА":
                    replyToUser = replyMessageService.getReplyMessage(chatId, "reply.inputLessons");
                    userDataCache.setUserCurrentBotState(userId, BotState.GET_WEEKDAY_AND_ADD_SCHEDULE);
                    break;
                case "НЕТ":
                    Schedule schedule = new Schedule();
                    schedule.setGroup(profileData.getGroup());
                    scheduleService.saveSchedule(schedule);
                    replyToUser = replyMessageService.getSuccessReplyMessage(chatId, "reply.groupWasAdded");
                    userDataCache.setUserCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
                    break;
                case "НАЗАД":
                    replyToUser = replyMessageService.getReplyMessage(chatId, "reply.askGroupToAdd");
                    userDataCache.setUserCurrentBotState(userId, BotState.SAVE_GROUP);
                    break;
                default:
                    replyToUser = replyMessageService.getWarningReplyMessage(chatId, "reply.сhooseAction");
                    userDataCache.setUserCurrentBotState(userId, BotState.ASK_FILLING_SCHEDULE);
                    break;
            }
        }
        //FIXME При нажатии на ИЗМЕНИТЬ не удалять расписание с БД
        if (botState.equals(BotState.GET_WEEKDAY_AND_ADD_SCHEDULE)) {
            if (message.equalsIgnoreCase("Отмена")) {
                userDataCache.setUserCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
                return replyMessageService.getReplyMessage(chatId, replyMessageService.getEmojiReplyText("reply.mainMenu.welcomeMessage", Emojis.HELP_MENU_WELCOME));
            }

                String[] lessons = message.split(",");
                if (lessons.length == 1 || lessons.length == 0 || lessons.length > 10) {
                    replyToUser = replyMessageService.getWarningReplyMessage(chatId, "reply.lessonsNotCorrect");
                    userDataCache.setUserCurrentBotState(userId, BotState.GET_WEEKDAY_AND_ADD_SCHEDULE);
                } else {
                    Schedule schedule = new Schedule(profileData.getGroup(), weekdays.get(countFillLessonsForWeekDay), lessons);

                    if (countFillLessonsForWeekDay == 6) {
                        replyToUser = replyMessageService.getSuccessReplyMessage(chatId, "reply.groupWasAdded");
                        userDataCache.setUserCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
                        countFillLessonsForWeekDay = 0;
                    }else {
                        countFillLessonsForWeekDay++;
                        scheduleService.saveSchedule(schedule);
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText(weekdays.get(countFillLessonsForWeekDay));
                        sendMessage.setChatId(String.valueOf(chatId));
                        replyToUser = sendMessage;
                        userDataCache.setUserCurrentBotState(userId, BotState.GET_WEEKDAY_AND_ADD_SCHEDULE);
                    }

                }

        }


        if (botState.equals(BotState.ACTIONS_GROUP)) {
            message = message.toUpperCase();

            switch (message) {
                case "УДАЛИТЬ":
                    scheduleService.deleteAllByGroup(profileData.getGroup());
                    replyToUser = replyMessageService.getSuccessReplyMessage(chatId, "reply.deleteGroup");
                    userDataCache.setUserCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
                    break;
                case "ИЗМЕНИТЬ":
                    scheduleService.deleteAllByGroup(profileData.getGroup());
                    replyToUser = replyMessageService.getReplyMessage(chatId, "reply.inputLessons");
                    userDataCache.setUserCurrentBotState(userId, BotState.GET_WEEKDAY_AND_ADD_SCHEDULE);
                    break;
                case "НАЗАД":
                    //scheduleService.delete(profileData.getGroup());
                    replyToUser = replyMessageService.getReplyMessage(chatId, replyMessageService.getEmojiReplyText("reply.mainMenu.welcomeMessage", Emojis.HELP_MENU_WELCOME));
                    userDataCache.setUserCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
                    break;
                default:
                    replyToUser = replyMessageService.getReplyMessage(chatId, "reply.groupFoundAskAction");
                    userDataCache.setUserCurrentBotState(userId, BotState.ACTIONS_GROUP);
                    break;
            }
        }

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.GROUP_HANDLER;
    }
}
