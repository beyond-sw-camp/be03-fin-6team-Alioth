package com.alioth.server.domain.schedule.repository;

import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM Schedule s WHERE s.salesMembers = :salesMember AND s.scheduleDel_YN = :scheduleDel_YN")
    List<Schedule> findAllBySalesMembersAndScheduleDel_YN(@Param("salesMember") SalesMembers salesMember, @Param("scheduleDel_YN") String scheduleDel_YN);

    @Query("SELECT s FROM Schedule s WHERE s.scheduleDel_YN = :scheduleDel_YN")
    List<Schedule> findAllByScheduleDel_YN(@Param("scheduleDel_YN") String scheduleDel_YN);

    @Query("SELECT s FROM Schedule s " +
            "JOIN s.salesMembers sm " +
            "JOIN sm.team t " +
            "WHERE t.teamManagerCode = :teamManagerCode " +
            "AND s.scheduleDel_YN = 'N'")
    List<Schedule> findAllTeamSchedule(@Param("teamManagerCode") Long teamManagerCode);


    @Query("SELECT s FROM Schedule s WHERE s.salesMembers = :salesMember AND s.scheduleDel_YN = 'N' OR s.scheduleId IN (" +
            "SELECT s2.scheduleId FROM Schedule s2 " +
            "JOIN s2.salesMembers sm " +
            "JOIN sm.team t " +
            "WHERE t.teamManagerCode = :teamManagerCode AND s2.share = 'true' AND s2.scheduleDel_YN = 'N') ")
    List<Schedule> findAllFPSchedule(@Param("salesMember") SalesMembers salesMember, @Param("teamManagerCode") Long teamManagerCode);
}
