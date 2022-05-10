package com.rottenbeetle.newsletterokpeip.buttons;

import com.rottenbeetle.newsletterokpeip.model.Schedule;
import com.rottenbeetle.newsletterokpeip.service.ScheduleService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SubscribeGroupButtons {
    private final ScheduleService scheduleService;

    public SubscribeGroupButtons(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    public InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();

        Set<String> groups = scheduleService.findAllGroupName();
        for (String group : groups) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(group);
            button.setCallbackData("SUBSCRIBE|" + group);
            keyboardButtonsRow1.add(button);
        }

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        if (groups.size() > 4) {
            List<InlineKeyboardButton> temp = new ArrayList<>();
            for (int i = 1; i < keyboardButtonsRow1.size(); i++) {
                temp.add(keyboardButtonsRow1.get(i-1));
                if (i % 4 == 0) {
                    rowList.add(temp);
                    temp = new ArrayList<>();
                }
            }

            if ((groups.size()) % 4 != 0) {
                temp = new ArrayList<>();
                for (int i = groups.size() - (groups.size() % 4); i < groups.size(); i++) {
                    temp.add(keyboardButtonsRow1.get(i));
                }
                rowList.add(temp);
            }

        } else {
            rowList.add(keyboardButtonsRow1);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
