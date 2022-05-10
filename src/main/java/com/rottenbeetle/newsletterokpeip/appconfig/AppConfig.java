package com.rottenbeetle.newsletterokpeip.appconfig;

import com.rottenbeetle.newsletterokpeip.botapi.NewsletterTelegramBot;
import com.rottenbeetle.newsletterokpeip.botapi.TelegramFacade;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = {"com.rottenbeetle.newsletterokpeip"})
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class AppConfig {

    private final BotConfig botConfig;

    public AppConfig(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Bean
    public NewsletterTelegramBot myTelegramBot(TelegramFacade telegramFacade){
        NewsletterTelegramBot newsletterTelegramBot = new NewsletterTelegramBot(telegramFacade);
//        System.out.println(webHookPath);
//        System.out.println(botToken);
//        System.out.println(botUserName);
        newsletterTelegramBot.setWebHookPath(botConfig.getWebHookPath());
        newsletterTelegramBot.setBotUserName(botConfig.getBotUserName());
        newsletterTelegramBot.setBotToken(botConfig.getBotToken());
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
