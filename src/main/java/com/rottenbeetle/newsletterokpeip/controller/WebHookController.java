package com.rottenbeetle.newsletterokpeip.controller;

import com.rottenbeetle.newsletterokpeip.NewsletterTelegramBot;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebHookController {
    private final NewsletterTelegramBot telegramBot;

    public WebHookController(NewsletterTelegramBot myTelegramBot) {
        this.telegramBot = myTelegramBot;
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){
        return telegramBot.onWebhookUpdateReceived(update);
    }
}


