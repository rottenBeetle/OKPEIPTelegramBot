package com.rottenbeetle.newsletterokpeip.botapi;

import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
    }

    public SendMessage handleUpdate(Update update){
        SendMessage replyMessage = null;

        Message message = update.getMessage();
        if(message!=null && message.hasText()){
            log.info("New message from User:{}, chatId: {}, with text: {}",
                    message.getFrom().getUserName(), message.getChatId(),message.getText());
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    //Переключение бота в определнное состояние
    private SendMessage handleInputMessage(Message message) {
        String inputMessage = message.getText();
        long userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMessage){
            case"/start":{
                botState = BotState.ASK_GROUP;
                break;
            }
            case "Узнать расписание":{
                botState = BotState.FILLING_PROFILE;
                break;
            }
            default:
                botState = userDataCache.getUserCurrentBotState(userId);
                break;
        }

        //Сохраняем в кэше для пользователя состояние бота
        userDataCache.setUserCurrentBotState(userId,botState);
        replyMessage = botStateContext.processInputMessage(botState,message);

        return replyMessage;

    }
}
