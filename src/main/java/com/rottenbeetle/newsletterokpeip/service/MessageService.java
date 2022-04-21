package com.rottenbeetle.newsletterokpeip.service;

import com.rottenbeetle.newsletterokpeip.model.Message;
import com.rottenbeetle.newsletterokpeip.model.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageService {
    List<Message> getAllMessages();
    void saveMessage(Message message);
    Message getMessageById(Long id);
    void deleteMessageById(Long id);
    List<Message> findLastMessages(int count,String groupName);
}
