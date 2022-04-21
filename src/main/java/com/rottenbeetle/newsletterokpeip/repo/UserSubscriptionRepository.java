package com.rottenbeetle.newsletterokpeip.repo;

import com.rottenbeetle.newsletterokpeip.model.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription,Long> {
    List<UserSubscription> findByChatIdAndGroupName(Long chatId,String groupName);
    List<UserSubscription> findAllByGroupName(String groupName);
}
