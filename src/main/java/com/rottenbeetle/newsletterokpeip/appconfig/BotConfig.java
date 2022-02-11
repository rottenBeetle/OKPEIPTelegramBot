package com.rottenbeetle.newsletterokpeip.appconfig;

import com.rottenbeetle.newsletterokpeip.NewsletterTelegramBot;
import com.rottenbeetle.newsletterokpeip.botapi.TelegramFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
/**
 * Создание бинов
 */
public class BotConfig {
    //Заполнятются из application.properties
    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Bean
    public NewsletterTelegramBot myTelegramBot(TelegramFacade telegramFacade){
        NewsletterTelegramBot newsletterTelegramBot = new NewsletterTelegramBot(telegramFacade);
//        System.out.println(webHookPath);
//        System.out.println(botToken);
//        System.out.println(botUserName);
        newsletterTelegramBot.setWebHookPath(webHookPath);
        newsletterTelegramBot.setBotUserName(botUserName);
        newsletterTelegramBot.setBotToken(botToken);
        return newsletterTelegramBot;
    }

    //Источник сообщений
    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
