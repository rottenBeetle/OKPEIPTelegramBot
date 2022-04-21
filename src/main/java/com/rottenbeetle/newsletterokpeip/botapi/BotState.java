package com.rottenbeetle.newsletterokpeip.botapi;

/**
 * Какие состояния есть у бота
 */
public enum BotState {
    ASK_GROUP,
    CURRENT_GROUP,
    SHOW_MAIN_MENU,
    SHOW_HELP_MENU,

    LAST_MESSAGES,
    ASK_COUNT_MESSAGES,
    SEND_LAST_MESSAGES,

    SENDING_MESSAGE,
    ASK_GROUP_FOR_MESSAGE,
    ASK_MESSAGE,
    SEND_MESSAGE,

    FILLING_PROFILE,
    GET_SCHEDULE,
    SUBSCRIBED,
    UNSUBSCRIBED
}
