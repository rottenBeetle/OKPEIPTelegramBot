package com.rottenbeetle.newsletterokpeip.botapi.handlers;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.InputMessageHandler;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.fillingprofile.UserProfileData;
import com.rottenbeetle.newsletterokpeip.buttons.GroupMessageButtons;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.entity.Schedule;
import com.rottenbeetle.newsletterokpeip.service.MainMenuService;
import com.rottenbeetle.newsletterokpeip.service.ReplyMessageService;
import com.rottenbeetle.newsletterokpeip.service.ScheduleService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
public class GetScheduleHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ScheduleService scheduleService;
    private MainMenuService mainMenuService;


    public GetScheduleHandler(UserDataCache userDataCache, ScheduleService scheduleService,MainMenuService mainMenuService) {
        this.userDataCache = userDataCache;
        this.scheduleService = scheduleService;
        this.mainMenuService = mainMenuService;
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
        long userId = inputMessage.getFrom().getId();
        long chatId = inputMessage.getChatId();
        String message = "";
        UserProfileData profileData = userDataCache.getUserProfileData(userId);

        List<Schedule> scheduleList = scheduleService.getAllSchedule(profileData.getGroup());
        for (Schedule schedule : scheduleList) {
            message += "----" + schedule.getWeekDay() + "----" + "\n";
            for (int i = 0; i < schedule.getLessons().length; i++) {
                message += i+1 + ". " + schedule.getLessons()[i] + "\n";
            }
        }
        return new SendMessage(String.valueOf(chatId),message);
    }

}
