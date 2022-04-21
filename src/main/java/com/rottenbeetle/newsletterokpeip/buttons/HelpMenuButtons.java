package com.rottenbeetle.newsletterokpeip.buttons;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
@Component
public class HelpMenuButtons {
        public InlineKeyboardMarkup getInlineMessageButtons() {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

            InlineKeyboardButton support = new InlineKeyboardButton();
            support.setText("Связаться с разработчиком");
            support.setUrl("https://vk.com/serojabiceps");

            InlineKeyboardButton project = new InlineKeyboardButton();
            project.setText("Посмотреть код проекта");
            project.setUrl("https://github.com/rottenBeetle/OKPEIPTelegramBot");

            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
            keyboardButtonsRow1.add(support);
            List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
            keyboardButtonsRow2.add(project);

            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            rowList.add(keyboardButtonsRow1);
            rowList.add(keyboardButtonsRow2);

            inlineKeyboardMarkup.setKeyboard(rowList);

            return inlineKeyboardMarkup;
        }
}
