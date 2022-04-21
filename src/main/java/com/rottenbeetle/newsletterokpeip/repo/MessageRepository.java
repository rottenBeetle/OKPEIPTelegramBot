package com.rottenbeetle.newsletterokpeip.repo;

import com.rottenbeetle.newsletterokpeip.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    @Query(value = "SELECT * FROM messages WHERE group_name=:#{#groupName} OR group_name='ВСЕМ' ORDER BY id DESC LIMIT :#{#count}", nativeQuery = true)
    List<Message> findLastMessages(@Param("count") int count,@Param("groupName") String groupName);
}
