package com.rottenbeetle.newsletterokpeip.service;

import com.rottenbeetle.newsletterokpeip.model.Schedule;

import java.util.List;
import java.util.Set;

public interface ScheduleService {
    List<Schedule> getAllScheduleForGroup(String group);
    Set<String> findAllGroupName();
    void saveSchedule(Schedule schedule);
    void deleteAllByGroup(String group);
    Schedule getScheduleByGroup(String group);
}
