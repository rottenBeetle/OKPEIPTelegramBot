package com.rottenbeetle.newsletterokpeip.botapi;

import com.rottenbeetle.newsletterokpeip.botapi.handlers.fillingprofile.UserProfileData;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.entity.User;
import com.rottenbeetle.newsletterokpeip.service.MainMenuService;
import com.rottenbeetle.newsletterokpeip.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;


@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private MainMenuService mainMenuService;
    private UserService userService;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache, MainMenuService mainMenuService, UserService userService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
        this.userService = userService;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        Message message = update.getMessage();

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
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
        BotState botState = BotState.ASK_GROUP;
        SendMessage replyMessage;
        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        switch (inputMessage) {
            case "/start":
            case "Выбрать другую группу": {
                botState = BotState.ASK_GROUP;
                break;
            }
            case "Расписание на неделю": {
                if (profileData.getGroup() != null)
                    botState = BotState.GET_SCHEDULE;
                break;
            }
            case "Подписаться на уведомления": {
                if (profileData.getGroup() != null)
                    botState = BotState.SUBSCRIBED;
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

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final long userId = buttonQuery.getFrom().getId();
        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotApiMethod<?> callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Воспользуйтесь главным меню",
                userDataCache.getUserCurrentBotState(userId));
        User user = userService.getUser(userId);

        //Кнопки для выбора группы
        if (buttonQuery.getData().equals("P_40")) {
            if (user.getGroup() != null && user.getGroup().equals("P_40")) {
                callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "", BotState.SUBSCRIBED);
            } else if(user.getGroup() != null && !user.getGroup().equals("P_40")){
                callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "", BotState.FILLING_PROFILE);
            } else {
                profileData.setGroup("P_40");
                userDataCache.saveUserProfileData(userId, profileData);
                callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Группа П-40 выбрана", BotState.ASK_GROUP);
            }
        } else if (buttonQuery.getData().equals("F_13")) {
            if (user.getGroup() != null && user.getGroup().equals("F_13")) {
                callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "", BotState.SUBSCRIBED);
            } else if(user.getGroup() != null && !user.getGroup().equals("F_13")){
                callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "", BotState.FILLING_PROFILE);
            } else {
                profileData.setGroup("F_13");
                userDataCache.saveUserProfileData(userId, profileData);
                callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Группа Ф-13 выбрана", BotState.ASK_GROUP);
            }
        } else if (buttonQuery.getData().equals("buttonIwillThink")) {
            callBackAnswer = sendAnswerCallbackQuery("Данная кнопка не поддерживается", true, buttonQuery);
        }

      /*  //From Gender choose buttons
        else if (buttonQuery.getData().equals("buttonMan")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setGender("М");
            userDataCache.saveUserProfileData(userId, userProfileData);
            userDataCache.setUserCurrentBotState(userId, BotState.ASK_COLOR);
            callBackAnswer = new SendMessage(chatId, "Твоя любимая цифра");
        } else if (buttonQuery.getData().equals("buttonWoman")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setGender("Ж");
            userDataCache.saveUserProfileData(userId, userProfileData);
            userDataCache.setUserCurrentBotState(userId, BotState.ASK_COLOR);
            callBackAnswer = new SendMessage(chatId, "Твоя любимая цифра");

        } else {
            userDataCache.setUserCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }*/
        return callBackAnswer;
    }

    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }
}
