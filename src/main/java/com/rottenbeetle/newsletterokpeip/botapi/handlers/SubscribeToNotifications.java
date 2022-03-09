package com.rottenbeetle.newsletterokpeip.botapi.handlers;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.InputMessageHandler;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.fillingprofile.UserProfileData;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.dao.UserDAOImpl;
import com.rottenbeetle.newsletterokpeip.entity.User;
import com.rottenbeetle.newsletterokpeip.service.MainMenuService;
import com.rottenbeetle.newsletterokpeip.service.ReplyMessageService;
import com.rottenbeetle.newsletterokpeip.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class SubscribeToNotifications implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessageService replyMessageService;
    private UserService userService;
    private MainMenuService mainMenuService;

    public SubscribeToNotifications(UserDataCache userDataCache, ReplyMessageService replyMessageService, UserService userService, MainMenuService mainMenuService) {
        this.userDataCache = userDataCache;
        this.replyMessageService = replyMessageService;
        this.userService = userService;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SUBSCRIBED;
    }

    private SendMessage processUsersInput(Message inputMessage) {

        long userId = inputMessage.getFrom().getId();
        long chatId = inputMessage.getChatId();
        SendMessage replyToUser;
        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        User user = new User(userId,chatId,profileData.getGroup(),inputMessage.getFrom().getUserName());
        userService.saveUser(user);
        //replyToUser = replyMessageService.getReplyMessage(chatId, "reply.responseUserSubscribed");
        //Отправить main menu через новый метод
        // callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Группа П-40 выбрана:");
        replyToUser = mainMenuService.getMainMenuMessage(chatId,"Вы подписались на группу",userDataCache.getUserCurrentBotState(userId));
        return replyToUser;
    }
}
