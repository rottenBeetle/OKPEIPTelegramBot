package com.rottenbeetle.newsletterokpeip.service;


import com.rottenbeetle.newsletterokpeip.model.Schedule;
import com.rottenbeetle.newsletterokpeip.repo.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    public Set<String> findAllGroupName() {
        Set<String> groups = scheduleRepository.findAllGroupName();
        return groups;
    }

    @Override
    public void saveSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    @Override
    @Transactional
    public void deleteAllByGroup(String group) {
        scheduleRepository.deleteAllByGroup(group);
    }

    @Override
    public Schedule getScheduleByGroup(String group) {
        return scheduleRepository.findByGroup(group);
    }
}
