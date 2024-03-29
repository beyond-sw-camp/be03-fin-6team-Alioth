package com.alioth.server.domain.schedule.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.dto.req.SalesMemberCreateReqDto;
import com.alioth.server.domain.member.repository.SalesMemberRepository;
import com.alioth.server.domain.member.service.SalesMemberService;
import com.alioth.server.domain.schedule.domain.Schedule;
import com.alioth.server.domain.schedule.domain.ScheduleType;
import com.alioth.server.domain.schedule.dto.req.ScheduleReqDto;

import com.alioth.server.domain.schedule.dto.res.ScheduleResDto;
import com.alioth.server.domain.schedule.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Rollback
@Transactional
@SpringBootTest
class ScheduleServiceTest {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private SalesMemberService salesMemberService;
    @Autowired
    private SalesMemberRepository salesMemberRepository;

    private Schedule schedule;

    private SalesMembers salesMembers;

    @BeforeEach
    void setUp() {
        ScheduleReqDto scheduleReqDto = ScheduleReqDto.builder()
                .scheduleStartTime(LocalDateTime.now())
                .scheduleEndTime(LocalDateTime.now().plusHours(1))
                .scheduleNote("Test Note")
                .scheduleType(ScheduleType.MEETING)
                .allDay("1")
                .build();

        SalesMemberCreateReqDto salesMemberCreateReqDto = SalesMemberCreateReqDto.builder()
                .email("jangdg2@naver.com") // 마스킹
                .phone("010-1234-1244") // 끝 4자리 마스킹
                .name("김민재2")
                .password("a1234567!")
                .birthDay("990123") // 마스킹
                .address("축신") // 마스킹
                .rank(SalesMemberType.FP)
                .build();
        SalesMembers salesMembers1 = salesMemberService.create(salesMemberCreateReqDto);
        this.salesMembers = salesMemberRepository.findBySalesMemberCode(salesMembers1.getSalesMemberCode())
                .orElseThrow(() -> new EntityNotFoundException("Saved salesMember not found"));
        ScheduleResDto savedScheduleResDto = scheduleService.save(scheduleReqDto, salesMembers.getSalesMemberCode());
        this.schedule = scheduleRepository.findById(savedScheduleResDto.scheduleId())
                .orElseThrow(() -> new EntityNotFoundException("Saved schedule not found"));
    }

    @Test
    @DisplayName("일정 저장 테스트")
    void save() {
        assertNotNull(schedule.getScheduleId());
    }

    @Test
    @DisplayName("일정 수정 테스트")
    void update() {
        ScheduleReqDto scheduleUpdateDto = ScheduleReqDto.builder()
                .scheduleStartTime(schedule.getScheduleStartTime())
                .scheduleEndTime(schedule.getScheduleEndTime())
                .scheduleNote("Updated Note")
                .scheduleType(schedule.getScheduleType())
                .allDay(schedule.getAllDay())
                .build();

        ScheduleResDto updatedSchedule = scheduleService.update(scheduleUpdateDto, schedule.getScheduleId(), salesMembers.getSalesMemberCode());
        assertEquals("Updated Note", updatedSchedule.scheduleNote());
    }

    @Test
    @DisplayName("일정 삭제 테스트")
    void delete() {
        ScheduleResDto deletedSchedule = scheduleService.delete(schedule.getScheduleId(), salesMembers.getSalesMemberCode());
        assertEquals("Y", deletedSchedule.del_yn());
    }

    @Test
    @DisplayName("특정 사용자의 모든 일정 조회 테스트")
    void list() {
        List<ScheduleResDto> schedules = scheduleService.list(salesMembers.getSalesMemberCode());
        assertFalse(schedules.isEmpty());
    }
}