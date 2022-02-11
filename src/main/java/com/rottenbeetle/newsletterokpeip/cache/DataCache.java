package com.rottenbeetle.newsletterokpeip.cache;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.fillingprofile.UserProfileData;

public interface DataCache {
    void setUserCurrentBotState(long userId, BotState botState);

    BotState getUserCurrentBotState(long userId);

     UserProfileData getUserProfileData(long userId);

     void saveUserProfileData(long userId, UserProfileData userProfileData);
}
