package com.rottenbeetle.newsletterokpeip.botapi.handlers.callbackquery;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserChatButtonStatus {
    SUBSCRIBED("Подписался"), UNSUBSCRIBED("Отписался");

    private String buttonStatus;

    @Override
    public String toString() {
        return buttonStatus;
    }
}
