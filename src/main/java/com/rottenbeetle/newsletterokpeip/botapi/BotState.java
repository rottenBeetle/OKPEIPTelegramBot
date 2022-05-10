package com.rottenbeetle.newsletterokpeip.botapi;

/**
 * Какие состояния есть у бота
 */
public enum BotState {
    ASK_GROUP,
    CURRENT_GROUP,
    SHOW_MAIN_MENU,
    SHOW_HELP_MENU,

    GROUP_HANDLER,
    ADD_GROUP,
    SAVE_GROUP,
    ACTIONS_GROUP,
    ASK_FILLING_SCHEDULE,
    GET_WEEKDAY_AND_ADD_SCHEDULE,


    LAST_MESSAGES,
    ASK_COUNT_MESSAGES,
    SEND_LAST_MESSAGES,

    SENDING_MESSAGE,
    ASK_GROUP_FOR_MESSAGE,
    ASK_MESSAGE,
    SEND_MESSAGE,


    GET_SCHEDULE,
    SUBSCRIBED,

    ADD_ADMIN,
    SAVE_ADMIN,
    ASK_ADMIN_ID

}
