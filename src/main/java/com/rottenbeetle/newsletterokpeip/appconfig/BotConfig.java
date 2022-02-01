package com.rottenbeetle.newsletterokpeip.appconfig;

import com.rottenbeetle.newsletterokpeip.NewsletterTelegramBot;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Bean
    public NewsletterTelegramBot myTelegramBot(){
        NewsletterTelegramBot newsletterTelegramBot = new NewsletterTelegramBot();
        System.out.println(webHookPath);
        System.out.println(botToken);
        System.out.println(botUserName);
        newsletterTelegramBot.setWebHookPath(webHookPath);
        newsletterTelegramBot.setBotUserName(botUserName);
        newsletterTelegramBot.setBotToken(botToken);


        return newsletterTelegramBot;
    }
}
