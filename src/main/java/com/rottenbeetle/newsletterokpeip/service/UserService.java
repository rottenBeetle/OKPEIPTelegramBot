package com.rottenbeetle.newsletterokpeip.service;

import com.rottenbeetle.newsletterokpeip.entity.User;

public interface UserService {
    void saveUser(User user);
    User getUser(long user_id);
}
