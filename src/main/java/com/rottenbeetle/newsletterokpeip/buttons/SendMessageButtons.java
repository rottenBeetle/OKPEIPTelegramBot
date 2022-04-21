package com.rottenbeetle.newsletterokpeip.buttons;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class SendMessageButtons {
    public InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonP40 = new InlineKeyboardButton();
        buttonP40.setText("П-40");
        InlineKeyboardButton buttonF13 = new InlineKeyboardButton();
        buttonF13.setText("Ф-13");
        InlineKeyboardButton buttonU14 = new InlineKeyboardButton();
        buttonU14.setText("Ю-14");
        InlineKeyboardButton buttonD12 = new InlineKeyboardButton();
        buttonD12.setText("Д-12");

        buttonP40.setCallbackData("SEND_MESSAGE|П-40");
        buttonF13.setCallbackData("SEND_MESSAGE|Ф-13");
        buttonU14.setCallbackData("SEND_MESSAGE|Ю_14");
        buttonD12.setCallbackData("SEND_MESSAGE|Д_12");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonP40);
        keyboardButtonsRow1.add(buttonF13);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonD12);
        keyboardButtonsRow2.add(buttonU14);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
