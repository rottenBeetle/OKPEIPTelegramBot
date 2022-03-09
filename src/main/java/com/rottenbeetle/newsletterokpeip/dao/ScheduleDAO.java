package com.rottenbeetle.newsletterokpeip.dao;

import com.rottenbeetle.newsletterokpeip.entity.Schedule;

import java.util.List;

public interface ScheduleDAO {
     List<Schedule> getAllSchedule(String group);
}
