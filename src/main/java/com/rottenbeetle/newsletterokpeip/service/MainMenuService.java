package com.rottenbeetle.newsletterokpeip.service;

import com.rottenbeetle.newsletterokpeip.model.Role;
import com.rottenbeetle.newsletterokpeip.model.User;
import com.rottenbeetle.newsletterokpeip.utils.Emojis;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Управляет отображением главного меню в чате.
 */
@Service
public class MainMenuService {
    private final LocaleMessageService localeMessageService;
    private final UserService userService;


    public MainMenuService(LocaleMessageService localeMessageService, UserService userService) {
        this.localeMessageService = localeMessageService;
        this.userService = userService;
    }

    public SendMessage getMainMenuMessage(final long chatId, final String textMessage) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard(chatId);
        final SendMessage mainMenuMessage =
                createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);
        return mainMenuMessage;
    }


     /*   public SendMessage getMainMenuMessageForSubscriber(final long chatId, final String textMessage) {
            final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();
            final SendMessage mainMenuMessage =
                    createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);
            return mainMenuMessage;
        }*/


    private ReplyKeyboardMarkup getMainMenuKeyboard(long chatId) {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        User user = userService.getUserById(chatId);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();

        row1.add(new KeyboardButton("Расписание\uD83D\uDC40"));
        row1.add(new KeyboardButton("Моя группа\uD83D\uDC65" ));
        row2.add(new KeyboardButton("Последние сообщения\uD83D\uDCEC"));
        row2.add(new KeyboardButton("Помощь\uD83C\uDD98"));
        keyboard.add(row1);
        keyboard.add(row2);

        if (user.getRoles().contains(Role.ADMIN)) {
            row3.add(new KeyboardButton("Отправить сообщение\uD83D\uDCE8"));
            row3.add(new KeyboardButton("Добавить группу/Изменить расписание\uD83D\uDEE0"));
            keyboard.add(row3);
            row4.add(new KeyboardButton("Добавить учителя\uD83D\uDCDA"));
            keyboard.add(row4);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private SendMessage createMessageWithKeyboard(final long chatId,
                                                  String textMessage,
                                                  final ReplyKeyboardMarkup replyKeyboardMarkup) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textMessage);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    public String getEmojiReplyText(String replyText, Emojis emoji) {
        return localeMessageService.getMessage(replyText, emoji);
    }

}

