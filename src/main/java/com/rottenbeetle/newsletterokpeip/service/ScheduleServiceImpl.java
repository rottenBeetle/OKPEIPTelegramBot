package com.rottenbeetle.newsletterokpeip.service;


import com.rottenbeetle.newsletterokpeip.model.Schedule;
import com.rottenbeetle.newsletterokpeip.repo.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public List<Schedule> getAllScheduleForGroup(String group){
        List<Schedule> schedules = scheduleRepository.findAllByGroup(group);
        Collections.sort(schedules);
        return schedules;
    }

    @Override
    public List<String> findAllGroupName() {
        List<String> groups = scheduleRepository.findAllGroupName();
        return groups;
    }
}
