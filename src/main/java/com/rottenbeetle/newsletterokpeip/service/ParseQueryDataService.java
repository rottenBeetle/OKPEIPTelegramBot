package com.rottenbeetle.newsletterokpeip.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

/*
   Делит на части callback запросов по символу ‘|’, которые поступают от кнопок.
 */
@Service
public class ParseQueryDataService {
    public String parseGroupNameFromSubscribeQuery(CallbackQuery callbackQuery) {
        return callbackQuery.getData().split("\\|")[1];
    }

    public String parseSubscriptionIdFromUnsubscribeQuery(CallbackQuery callbackQuery) {
        return callbackQuery.getData().split("\\|")[1];
    }
}
