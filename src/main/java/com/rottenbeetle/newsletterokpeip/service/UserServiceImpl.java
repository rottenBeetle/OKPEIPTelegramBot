package com.rottenbeetle.newsletterokpeip.service;

import com.rottenbeetle.newsletterokpeip.dao.UserDAO;
import com.rottenbeetle.newsletterokpeip.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDAO userDAO;

    @Override
    @Transactional
    public void saveUser(User user) {
        userDAO.saveUser(user);
    }

    @Override
    @Transactional
    public User getUser(long user_id) {
        return userDAO.getUser(user_id);
    }
}
