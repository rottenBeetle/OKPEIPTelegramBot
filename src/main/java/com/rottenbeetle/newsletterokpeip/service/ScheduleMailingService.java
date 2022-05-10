package com.rottenbeetle.newsletterokpeip.service;

import com.rottenbeetle.newsletterokpeip.model.Schedule;
import com.rottenbeetle.newsletterokpeip.model.UserSubscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//         ┌───────────── second (0-59)
//         │ ┌───────────── minute (0 - 59)
//         │ │ ┌───────────── hour (0 - 23)
//         │ │ │ ┌───────────── day of the month (1 - 31)
//         │ │ │ │ ┌───────────── month (1 - 12) (or JAN-DEC)
//         │ │ │ │ │ ┌───────────── day of the week (0 - 7)
//         │ │ │ │ │ │          (or MON-SUN -- 0 or 7 is Sunday)
//         │ │ │ │ │ │
//         * * * * * *

@Service
public class ScheduleMailingService {
    private final UserSubscriptionService userSubscriptionService;
    private final ScheduleService scheduleService;
    @Value("#{'${telegrambot.botToken}'}")
    private String token;
    private HashMap<Integer, String> weekdays;

    public ScheduleMailingService(UserSubscriptionService userSubscriptionService, ScheduleService scheduleService) {
        this.userSubscriptionService = userSubscriptionService;
        this.scheduleService = scheduleService;
        this.weekdays = new HashMap<>();
        weekdays.put(1, "Понедельник");
        weekdays.put(2, "Вторник");
        weekdays.put(3, "Среда");
        weekdays.put(4, "Четверг");
        weekdays.put(5, "Пятница");
        weekdays.put(6, "Суббота");
    }

    @Scheduled(cron = "0 0 7 * * *",zone = "GMT+3")
    public void sendOut() {
        List<UserSubscription> userSubscriptions = userSubscriptionService.getAllSubscription();

        for (UserSubscription userSubscription : userSubscriptions) {
            Date date = new Date();
            String currentWeekday = weekdays.get(date.getDay());
            Schedule schedule = scheduleService.findByGroupAndWeekDay(userSubscription.getGroupName(), currentWeekday);
            String scheduleForWeekday = "";
            if (schedule != null) {
                scheduleForWeekday = "✨Сегодня - " + schedule.getWeekDay() + "%0A" + "Ваше расписание:✨" + "%0A";
                for (int i = 0; i < schedule.getLessons().length; i++) {
                    scheduleForWeekday += i + 1 + "." + schedule.getLessons()[i] + "%0A";
                }
            } else {
                scheduleForWeekday = "✨Сегодня у вас выходной✨";
            }

            try {
                URL url = new URL("https://api.telegram.org/bot" + token
                        + "/sendMessage" + "?chat_id=" + userSubscription.getChatId() + "&text=" + scheduleForWeekday);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                int statusCode = http.getResponseCode();
                System.out.println(statusCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
