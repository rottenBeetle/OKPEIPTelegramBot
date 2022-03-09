package com.rottenbeetle.newsletterokpeip.dao;

import com.rottenbeetle.newsletterokpeip.entity.Schedule;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ScheduleDAOImpl implements ScheduleDAO{
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public List<Schedule> getAllSchedule(String group) {
        Session session = sessionFactory.getCurrentSession();
        Query<Schedule> query = session.createQuery("from Schedule where groupname = :groupName");
        query.setParameter("groupName",group);
        List<Schedule> scheduleList = query.list();
        return scheduleList;
    }
}
