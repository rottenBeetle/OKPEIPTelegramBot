package com.rottenbeetle.newsletterokpeip.service;

import com.rottenbeetle.newsletterokpeip.botapi.BotState;
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

        public SendMessage getMainMenuMessage(final long chatId, final String textMessage, BotState botState) {
            final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard(botState,chatId);
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


        private ReplyKeyboardMarkup getMainMenuKeyboard(BotState botState,final long chat_id) {

            final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(false);

            List<KeyboardRow> keyboard = new ArrayList<>();

            KeyboardRow row1 = new KeyboardRow();
            KeyboardRow row2 = new KeyboardRow();
            KeyboardRow row3 = new KeyboardRow();

            if (botState == BotState.SUBSCRIBED){
                row1.add(new KeyboardButton("Отписаться от уведомлений"));
                keyboard.add(row1);
            }else if (botState == BotState.ASK_GROUP){
                row1.add(new KeyboardButton("Подписаться на уведомления"));
                keyboard.add(row1);
            }

            row2.add(new KeyboardButton("Расписание на неделю"));
            row3.add(new KeyboardButton("Выбрать другую группу"));

            keyboard.add(row2);
            keyboard.add(row3);

            //Функционал для админа
            if (chat_id == 693197292){
                KeyboardRow row4 = new KeyboardRow();
                KeyboardRow row5 = new KeyboardRow();
                row4.add(new KeyboardButton("Написать сообщение группе"));
                row5.add(new KeyboardButton("Написать сообщение для всех"));
                keyboard.add(row4);
                keyboard.add(row5);
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
    }

