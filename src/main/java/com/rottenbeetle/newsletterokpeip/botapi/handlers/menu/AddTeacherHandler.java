package com.rottenbeetle.newsletterokpeip.botapi.handlers.menu;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.InputMessageHandler;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.askgroup.UserProfileData;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.model.Role;
import com.rottenbeetle.newsletterokpeip.model.User;
import com.rottenbeetle.newsletterokpeip.service.ReplyMessageService;
import com.rottenbeetle.newsletterokpeip.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/*
    Добавляет chat_id нового учителя в базу данных
 */
@Component
public class AddTeacherHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final ReplyMessageService replyMessageService;
    private final UserService userService;
    @Value("#{'${telegrambot.botToken}'}")
    private String token;

    public AddTeacherHandler(UserDataCache userDataCache, ReplyMessageService replyMessageService, UserService userService) {
        this.userDataCache = userDataCache;
        this.replyMessageService = replyMessageService;
        this.userService = userService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUserCurrentBotState(message.getFrom().getId()).equals(BotState.ADD_ADMIN)) {
            userDataCache.setUserCurrentBotState(message.getFrom().getId(), BotState.ASK_ADMIN_ID);
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

        if (botState.equals(BotState.ASK_ADMIN_ID)) {
            replyToUser = replyMessageService.getReplyMessage(chatId, "reply.askAdminId");
            userDataCache.setUserCurrentBotState(userId, BotState.SAVE_ADMIN);
        }
        if (botState.equals(BotState.SAVE_ADMIN)) {
            User user = userService.getByUserName(message);
            if (user != null) {
                if (user.getRoles().contains(Role.ADMIN)) {
                    replyToUser = replyMessageService.getSuccessReplyMessage(chatId, "reply.userAlreadyAdmin");
                } else {
                    Set<Role> roles = new HashSet<>();
                    roles.add(Role.ADMIN);
                    user.setRoles(roles);
                    userService.saveUser(user);

                    try {
                        URL url = new URL("https://api.telegram.org/bot" + token
                                + "/sendMessage" + "?chat_id=" + user.getChat_id() + "&text=Вам присвоены права админа!");
                        HttpURLConnection http = (HttpURLConnection) url.openConnection();
                        int statusCode = http.getResponseCode();
                        System.out.println(statusCode);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    replyToUser = replyMessageService.getSuccessReplyMessage(chatId, "reply.adminWasAdded");
                }
            } else {
                replyToUser = replyMessageService.getWarningReplyMessage(chatId, "reply.userNotFound");
            }
            userDataCache.setUserCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }
        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ADD_ADMIN;
    }
}
