package com.rottenbeetle.newsletterokpeip.service;

import com.rottenbeetle.newsletterokpeip.model.Schedule;

import java.util.List;

public interface ScheduleService {
    List<Schedule> getAllScheduleForGroup(String group);
    List<String> findAllGroupName();
}
