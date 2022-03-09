package com.rottenbeetle.newsletterokpeip.service;

import com.rottenbeetle.newsletterokpeip.entity.Schedule;

import java.util.List;

public interface ScheduleService {
    List<Schedule> getAllSchedule(String group);
}
