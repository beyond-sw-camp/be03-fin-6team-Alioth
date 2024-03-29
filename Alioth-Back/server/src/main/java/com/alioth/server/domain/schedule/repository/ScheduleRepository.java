package com.alioth.server.domain.schedule.repository;

import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllBySalesMembers(SalesMembers salesMembers);
}
