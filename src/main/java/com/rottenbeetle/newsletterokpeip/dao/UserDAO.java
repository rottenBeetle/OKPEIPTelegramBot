package com.rottenbeetle.newsletterokpeip.dao;

import com.rottenbeetle.newsletterokpeip.entity.User;

public interface UserDAO {
    void saveUser(User user);
    User getUser(long user_id);
}
