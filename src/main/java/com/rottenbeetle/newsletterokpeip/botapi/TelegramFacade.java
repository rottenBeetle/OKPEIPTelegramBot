package com.rottenbeetle.newsletterokpeip.botapi;

import com.rottenbeetle.newsletterokpeip.botapi.handlers.askgroup.UserProfileData;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.callbackquery.CallbackQueryFacade;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.model.Role;
import com.rottenbeetle.newsletterokpeip.model.User;
import com.rottenbeetle.newsletterokpeip.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;


@Service
@Slf4j
public class TelegramFacade {
    private final BotStateContext botStateContext;
    private final UserDataCache userDataCache;
    private final CallbackQueryFacade callbackQueryFacade;
    private final UserService userService;


    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache, CallbackQueryFacade callbackQueryFacade, UserService userService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.callbackQueryFacade = callbackQueryFacade;
        this.userService = userService;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;
        Message message = update.getMessage();

        if (update.hasCallbackQuery()) {
            log.info("New callbackQuery from User: {} with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    update.getCallbackQuery().getData());
            return callbackQueryFacade.processCallbackQuery(update.getCallbackQuery());
        }

        if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatId: {}, with text: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    //Переключение бота в определнное состояние
    private SendMessage handleInputMessage(Message message) {
        String inputMessage = message.getText();
        long userId = message.getFrom().getId();
        User user = userService.getUserById(userId);

        BotState botState = BotState.ASK_GROUP;
        SendMessage replyMessage;
        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        switch (inputMessage) {
            case "/start": {
                botState = BotState.ASK_GROUP;
                break;
            }
            case "Моя группа\uD83D\uDC65": {
                botState = BotState.CURRENT_GROUP;
                break;
            }
            case "Расписание\uD83D\uDC40": {
                botState = BotState.GET_SCHEDULE;
                break;
            }
            case "Последние сообщения\uD83D\uDCEC": {
                botState = BotState.LAST_MESSAGES;
                break;
            }
            case "Помощь\uD83C\uDD98": {
                botState = BotState.SHOW_HELP_MENU;
                break;
            }
            case "Отправить сообщение\uD83D\uDCE8": {
                if (user.getRoles().contains(Role.ADMIN))
                    botState = BotState.SENDING_MESSAGE;
                break;
            }
            case "Добавить группу/Изменить расписание\uD83D\uDEE0": {
                if (user.getRoles().contains(Role.ADMIN))
                    botState = BotState.GROUP_HANDLER;
                break;
            }
            case "Добавить учителя\uD83D\uDCDA": {
                if (user.getRoles().contains(Role.ADMIN))
                    botState = BotState.ADD_ADMIN;
                break;
            }
            default:
                botState = userDataCache.getUserCurrentBotState(userId);
                break;
        }

        //Сохраняем в кэше для пользователя состояние бота
        userDataCache.setUserCurrentBotState(userId, botState);
        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }
}
