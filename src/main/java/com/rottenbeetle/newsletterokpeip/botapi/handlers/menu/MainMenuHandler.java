package com.rottenbeetle.newsletterokpeip.botapi.handlers.menu;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
import com.rottenbeetle.newsletterokpeip.botapi.handlers.InputMessageHandler;
import com.rottenbeetle.newsletterokpeip.service.MainMenuService;
import com.rottenbeetle.newsletterokpeip.service.ReplyMessageService;
import com.rottenbeetle.newsletterokpeip.utils.Emojis;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/*
   Показывает главное меню
 */
@Component
public class MainMenuHandler implements InputMessageHandler {
    private final ReplyMessageService messagesService;
    private final MainMenuService mainMenuService;
    private final ReplyMessageService messageService;

    public MainMenuHandler(ReplyMessageService messagesService, MainMenuService mainMenuService, ReplyMessageService messageService) {
        this.messagesService = messagesService;
        this.mainMenuService = mainMenuService;
        this.messageService = messageService;
    }

    @Override
    public SendMessage handle(Message message) {
        return mainMenuService.getMainMenuMessage(message.getChatId(), messageService.getEmojiReplyText("reply.mainMenu.welcomeMessage", Emojis.HELP_MENU_WELCOME));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }


}
