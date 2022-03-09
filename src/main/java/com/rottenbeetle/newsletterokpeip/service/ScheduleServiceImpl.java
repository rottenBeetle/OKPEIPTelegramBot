package com.rottenbeetle.newsletterokpeip.service;

import com.rottenbeetle.newsletterokpeip.dao.ScheduleDAO;
import com.rottenbeetle.newsletterokpeip.entity.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleDAO scheduleDAO;

    @Override
    @Transactional
    public List<Schedule> getAllSchedule(String group) {

        return scheduleDAO.getAllSchedule(group);
    }
}
