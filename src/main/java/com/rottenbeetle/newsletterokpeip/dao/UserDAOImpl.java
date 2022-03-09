package com.rottenbeetle.newsletterokpeip.dao;

import com.rottenbeetle.newsletterokpeip.entity.User;
import com.rottenbeetle.newsletterokpeip.service.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public void saveUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(user);
    }

    @Override
    public User getUser(long user_id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class,user_id);
        return user;
    }
}
