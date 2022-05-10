package com.rottenbeetle.newsletterokpeip.repo;

import com.rottenbeetle.newsletterokpeip.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User getByUserName(String username);
}
