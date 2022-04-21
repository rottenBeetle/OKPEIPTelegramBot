package com.rottenbeetle.newsletterokpeip.appconfig;

import com.rottenbeetle.newsletterokpeip.botapi.NewsletterTelegramBot;
import com.rottenbeetle.newsletterokpeip.botapi.TelegramFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "telegrambot")
@Getter
@Setter
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;
}
