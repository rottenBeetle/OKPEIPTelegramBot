package com.rottenbeetle.newsletterokpeip.service;

import com.rottenbeetle.newsletterokpeip.model.User;
import com.rottenbeetle.newsletterokpeip.model.UserSubscription;
import com.rottenbeetle.newsletterokpeip.repo.UserSubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserSubscriptionServiceImpl implements UserSubscriptionService {
    private UserSubscriptionRepository subscriptionRepository;

    public UserSubscriptionServiceImpl(UserSubscriptionRepository userSubscriptionRepository) {
        this.subscriptionRepository = userSubscriptionRepository;
    }

    @Override
    public List<UserSubscription> getAllSubscription(){
        return subscriptionRepository.findAll();
    }

    @Override
    public void saveUserSubscription(UserSubscription userSubscription){
        subscriptionRepository.save(userSubscription);
    }

    @Override
    public void deleteUserSubscription(Long id){
        subscriptionRepository.deleteById(id);
    }

    @Override
    public boolean hasSubscription(UserSubscription userSubscription){
        return subscriptionRepository.findByChatIdAndGroupName(userSubscription.getChatId(),
                userSubscription.getGroupName()).size() > 0;
    }

    @Override
    public List<UserSubscription> findAllByGroupName(String groupName) {
        return subscriptionRepository.findAllByGroupName(groupName);
    }

    @Override
    public UserSubscription getUsersSubscriptionById(Long id){
        Optional<UserSubscription> optional = subscriptionRepository.findById(id);
        UserSubscription userSubscription = null;
        if (optional.isPresent()){
            userSubscription = optional.get();
        }
//        else {
//            throw new RuntimeException("User subscription not found for id :: " + id);
//        }
        return userSubscription;
    }
}
