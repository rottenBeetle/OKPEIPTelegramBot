package com.rottenbeetle.newsletterokpeip.botapi;

import com.rottenbeetle.newsletterokpeip.botapi.handlers.askgroup.UserProfileData;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.callbackquery.CallbackQueryFacade;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.model.User;
import com.rottenbeetle.newsletterokpeip.service.MainMenuService;
import com.rottenbeetle.newsletterokpeip.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class TelegramFacade {
    private final BotStateContext botStateContext;
    private final UserDataCache userDataCache;
    private final CallbackQueryFacade callbackQueryFacade;
    //FIXME Добавить возможность добавления админа
    @Value("#{'${listOfIdAdmins}'.split(',')}")
    private List<Long> listOfOdAdmins;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache,CallbackQueryFacade callbackQueryFacade) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.callbackQueryFacade = callbackQueryFacade;
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
        long chatId = message.getChatId();
        long userId = message.getFrom().getId();
        BotState botState = BotState.ASK_GROUP;
        SendMessage replyMessage;
        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        switch (inputMessage) {
            case "/start": {
                botState = BotState.ASK_GROUP;
                break;
            }
            case "Моя группа": {
                botState = BotState.CURRENT_GROUP;
                break;
            }
            case "Расписание": {
                    botState = BotState.GET_SCHEDULE;
                break;
            }
            case "Последние сообщения": {
                botState = BotState.LAST_MESSAGES;
                break;
            }
            case "Помощь": {
                botState = BotState.SHOW_HELP_MENU;
                break;
            }
            case "Отправить сообщение": {
                if (listOfOdAdmins.contains(chatId))
                botState = BotState.SENDING_MESSAGE;
                break;
            }
            case "Добавить группу/Изменить расписание": {
                if (listOfOdAdmins.contains(chatId))
                    botState = BotState.GROUP_HANDLER;
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
