package com.rottenbeetle.newsletterokpeip.service;

import com.rottenbeetle.newsletterokpeip.model.UserSubscription;

import java.util.List;

public interface UserSubscriptionService {
    List<UserSubscription> getAllSubscription();
    void saveUserSubscription(UserSubscription userSubscription);
    void deleteUserSubscription(Long id);
    UserSubscription getUsersSubscriptionById(Long id);
    boolean hasSubscription(UserSubscription userSubscription);
    List<UserSubscription> findAllByGroupName(String groupName);
}
