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

@Component
public class EditGroupHandler implements InputMessageHandler {
    private final ReplyMessageService replyMessageService;
    private final UserDataCache userDataCache;
    private final MessageService messageService;
    private final UserSubscriptionService userSubscriptionService;
    private final ScheduleService scheduleService;
    @Value("#{'${telegrambot.botToken}'}")
    private String token;
    private static int countFillLessonsForWeekDay;
    private HashMap<Integer, String> weekdays;

    public EditGroupHandler(ReplyMessageService replyMessageService, UserDataCache userDataCache, MessageService messageService, UserSubscriptionService userSubscriptionService, ScheduleService scheduleService) {
        this.replyMessageService = replyMessageService;
        this.userDataCache = userDataCache;
        this.messageService = messageService;
        this.userSubscriptionService = userSubscriptionService;
        this.scheduleService = scheduleService;
        this.weekdays = new HashMap<>();
        weekdays.put(0, "Понедельник");
        weekdays.put(1, "Вторник");
        weekdays.put(2, "Среда");
        weekdays.put(3, "Четверг");
        weekdays.put(4, "Пятница");
        weekdays.put(5, "Суббота");
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

        if (botState.equals(BotState.GET_WEEKDAY_AND_ADD_SCHEDULE)) {
            if (countFillLessonsForWeekDay == 5) {
                replyToUser = replyMessageService.getSuccessReplyMessage(chatId, "reply.groupWasAdded");
                userDataCache.setUserCurrentBotState(userId, BotState.SHOW_HELP_MENU);
                countFillLessonsForWeekDay=0;
            } else {
                String[] lessons = message.split(",");
                if (lessons.length == 1 || lessons.length == 0) {
                    replyToUser = replyMessageService.getWarningReplyMessage(chatId, "reply.lessonsNotCorrect");
                    userDataCache.setUserCurrentBotState(userId, BotState.GET_WEEKDAY_AND_ADD_SCHEDULE);
                } else {
                    Schedule schedule = new Schedule(profileData.getGroup(), weekdays.get(countFillLessonsForWeekDay), lessons);
                    scheduleService.saveSchedule(schedule);
                    countFillLessonsForWeekDay++;
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText("                     -----" + weekdays.get(countFillLessonsForWeekDay) + "-----");
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
                    scheduleService.deleteAllByGroup(profileData.getGroup());
                    replyToUser = replyMessageService.getSuccessReplyMessage(chatId, "reply.mainMenu.welcomeMessage");
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
