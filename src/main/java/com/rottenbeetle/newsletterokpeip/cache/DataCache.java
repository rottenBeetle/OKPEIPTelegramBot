package com.rottenbeetle.newsletterokpeip.cache;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.askgroup.UserProfileData;
import com.rottenbeetle.newsletterokpeip.model.Schedule;

import java.util.List;

public interface DataCache {
    void setUserCurrentBotState(long userId, BotState botState);

    BotState getUserCurrentBotState(long userId);

     UserProfileData getUserProfileData(long userId);

     void saveUserProfileData(long userId, UserProfileData userProfileData);

     List<Schedule> getSchedule(Long chatId);
}
