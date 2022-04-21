package com.rottenbeetle.newsletterokpeip.repo;

import com.rottenbeetle.newsletterokpeip.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findAllByGroup(String group);
    @Query(value = "SELECT groupname FROM Schedule", nativeQuery = true)
    List<String> findAllGroupName();
}
