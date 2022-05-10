package com.rottenbeetle.newsletterokpeip.botapi;

import com.rottenbeetle.newsletterokpeip.botapi.handlers.InputMessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Определяет обработчики сообщений для каждого состояния
 */
@Component
public class BotStateContext{
    //По названию состояния получим нужный обработчик
    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(),handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message){
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (isSendingMessageState(currentState)){
            return  messageHandlers.get(BotState.SENDING_MESSAGE);
        }

        if (isGetLastMessagesState(currentState)){
            return  messageHandlers.get(BotState.LAST_MESSAGES);
        }

        if (isEditingGroup(currentState)){
            return  messageHandlers.get(BotState.GROUP_HANDLER);
        }
        if (isAddAdmin(currentState)){
            return  messageHandlers.get(BotState.ADD_ADMIN);
        }

        return messageHandlers.get(currentState);
    }


    private boolean isAddAdmin(BotState currentState) {
        switch (currentState){
            case ADD_ADMIN:
            case SAVE_ADMIN:
                return true;
            default:
                return false;
        }
    }

    private boolean isEditingGroup(BotState currentState) {
        switch (currentState){
            case GROUP_HANDLER:
            case ADD_GROUP:
            case SAVE_GROUP:
            case ASK_FILLING_SCHEDULE:
            case ACTIONS_GROUP:
            case GET_WEEKDAY_AND_ADD_SCHEDULE:

                return true;
            default:
                return false;
        }
    }

    private boolean isSendingMessageState(BotState currentState) {
        switch (currentState){
            case SENDING_MESSAGE:
            case ASK_GROUP_FOR_MESSAGE:
            case ASK_MESSAGE:
            case SEND_MESSAGE:
                return true;
            default:
                return false;
        }
    }

    private boolean isGetLastMessagesState(BotState currentState) {
        switch (currentState){
            case LAST_MESSAGES:
            case ASK_COUNT_MESSAGES:
            case SEND_LAST_MESSAGES:
                return true;
            default:
                return false;
        }
    }
}
