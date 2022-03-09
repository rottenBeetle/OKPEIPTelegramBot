package com.rottenbeetle.newsletterokpeip.botapi.handlers.askgroup;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.InputMessageHandler;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.fillingprofile.UserProfileData;
import com.rottenbeetle.newsletterokpeip.buttons.GroupMessageButtons;
import com.rottenbeetle.newsletterokpeip.cache.UserDataCache;
import com.rottenbeetle.newsletterokpeip.service.MainMenuService;
import com.rottenbeetle.newsletterokpeip.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;


/**
 * Спрашивает пользователя его группу.
 */
@Component
@Slf4j
public class AskGroupHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessageService replyMessageService;
    private GroupMessageButtons groupMessageButtons;


    public AskGroupHandler(UserDataCache userDataCache, ReplyMessageService replyMessageService,GroupMessageButtons groupMessageButtons) {
        this.userDataCache = userDataCache;
        this.replyMessageService = replyMessageService;
        this.groupMessageButtons = groupMessageButtons;

    }


    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_GROUP;
    }

    private SendMessage processUsersInput(Message inputMessage) {
        long userId = inputMessage.getFrom().getId();
        long chatId = inputMessage.getChatId();
        SendMessage replyToUser;
        replyToUser = replyMessageService.getReplyMessage(chatId, "reply.askGroup");
        replyToUser.setReplyMarkup(groupMessageButtons.getInlineMessageButtons());

        return replyToUser;
    }


}
