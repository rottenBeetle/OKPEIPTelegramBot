package com.rottenbeetle.newsletterokpeip.service;

import com.rottenbeetle.newsletterokpeip.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    void saveUser(User user);
    User getUserById(Long id);
    User getByUserName(String username);
}
