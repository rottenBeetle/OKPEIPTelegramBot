package com.rottenbeetle.newsletterokpeip.botapi.handlers.menu;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.InputMessageHandler;
import com.rottenbeetle.newsletterokpeip.buttons.HelpMenuButtons;
import com.rottenbeetle.newsletterokpeip.model.Schedule;
import com.rottenbeetle.newsletterokpeip.model.UserSubscription;
import com.rottenbeetle.newsletterokpeip.service.MainMenuService;
import com.rottenbeetle.newsletterokpeip.service.ReplyMessageService;
import com.rottenbeetle.newsletterokpeip.utils.Emojis;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
public class HelpMenuHandler implements InputMessageHandler {
    private final MainMenuService mainMenuService;
    private final ReplyMessageService messagesService;
    private final HelpMenuButtons helpMenuButtons;

    public HelpMenuHandler(MainMenuService mainMenuService, ReplyMessageService messagesService, HelpMenuButtons helpMenuButtons) {
        this.mainMenuService = mainMenuService;
        this.messagesService = messagesService;
        this.helpMenuButtons = helpMenuButtons;
    }

    @Override
    public SendMessage handle(Message message) {
      return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message inputMessage) {
        SendMessage replyToUser = mainMenuService.getMainMenuMessage(inputMessage.getChatId(),
                messagesService.getEmojiReplyText("reply.helpMenu.welcomeMessage", Emojis.HELP_MENU_WELCOME));
        replyToUser.setReplyMarkup(helpMenuButtons.getInlineMessageButtons());
        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_HELP_MENU;
    }
}
