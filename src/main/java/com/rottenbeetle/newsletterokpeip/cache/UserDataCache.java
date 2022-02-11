package com.rottenbeetle.newsletterokpeip.cache;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.fillingprofile.UserProfileData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * userBotStates - user_id и состояние бота
 * userProfileData - user_id и информация по анкете
 */
@Component
public class UserDataCache implements DataCache{
    private Map<Long, BotState> userBotStates = new HashMap<>();
    private Map<Long, UserProfileData> userProfileData = new HashMap<>();

    @Override
    public void setUserCurrentBotState(long userId, BotState botState) {
        userBotStates.put(userId,botState);
    }

    @Override
    public BotState getUserCurrentBotState(long userId) {
        //Получаем состояние бота по user_id
        BotState botState = userBotStates.get(userId);
        //Если состояние бота не определнно(только начал общение)
        if(botState == null)
            botState = BotState.ASK_GROUP;

        return botState;
    }

    @Override
    public UserProfileData getUserProfileData(long userId) {
        UserProfileData userProfileData = this.userProfileData.get(userId);
        if(userProfileData == null){
            userProfileData = new UserProfileData();
        }
        return  userProfileData;
    }

    @Override
    public void saveUserProfileData(long userId, UserProfileData userProfileData) {
        this.userProfileData.put(userId,userProfileData);
    }
}
