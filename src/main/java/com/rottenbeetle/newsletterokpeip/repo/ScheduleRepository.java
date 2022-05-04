package com.rottenbeetle.newsletterokpeip.repo;

import com.rottenbeetle.newsletterokpeip.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findAllByGroup(String group);
    @Query(value = "SELECT groupname FROM Schedule", nativeQuery = true)
    Set<String> findAllGroupName();
    void deleteAllByGroup(String group);
    Schedule findByGroup(String group);
}
