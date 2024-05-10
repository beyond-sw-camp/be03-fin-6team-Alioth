package com.alioth.server.domain.schedule.service;

import com.alioth.server.common.domain.TypeChange;
import com.alioth.server.domain.member.domain.SalesMemberType;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.member.service.SalesMemberService;
import com.alioth.server.domain.schedule.domain.Schedule;
import com.alioth.server.domain.schedule.dto.req.ScheduleReqDto;
import com.alioth.server.domain.schedule.dto.res.ScheduleResDto;
import com.alioth.server.domain.schedule.repository.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final TypeChange typeChange;
    private final SalesMemberService salesMemberService;

    public Schedule findById(Long scheduleId){
        return scheduleRepository.findById(scheduleId).orElseThrow(()->new EntityNotFoundException("존재하지 않는 일정입니다."));
    }

    public void scheduleException(Schedule schedule, SalesMembers salesMembers){
        if(!Objects.equals(schedule.getSalesMembers(), salesMembers)){
            throw new IllegalArgumentException("일정의 작성자가 아닙니다.");
        }
    }

    public ScheduleResDto save(ScheduleReqDto scheduleReqDto, Long sm_code) {
        SalesMembers salesMembers = salesMemberService.findBySalesMemberCode(sm_code);
        return typeChange.ScheduleToScheduleResDto(
                scheduleRepository.save(
                        typeChange.ScheduleCreateDtoToSchedule(scheduleReqDto, salesMembers)
                )
        );
    }

    public ScheduleResDto update(ScheduleReqDto scheduleReqDto, Long scheduleId, Long sm_code) {
        Schedule schedule = this.findById(scheduleId);
        SalesMembers salesMembers = salesMemberService.findBySalesMemberCode(sm_code);
        scheduleException(schedule, salesMembers);
        schedule.update(scheduleReqDto);
        return typeChange.ScheduleToScheduleResDto(schedule);
    }

    public ScheduleResDto delete(Long scheduleId, Long sm_code) {
        Schedule schedule = this.findById(scheduleId);
        SalesMembers salesMembers = salesMemberService.findBySalesMemberCode(sm_code);
        scheduleException(schedule, salesMembers);
        schedule.delete();
        return typeChange.ScheduleToScheduleResDto(schedule);
    }

    public List<ScheduleResDto> list(Long sm_code) {
        SalesMembers salesMembers = salesMemberService.findBySalesMemberCode(sm_code);

        if(salesMembers.getRank() == SalesMemberType.HQ){
            return scheduleRepository.findAllByScheduleDel_YN("N").stream()
                    .map(typeChange::ScheduleToScheduleResDto).toList();
        }

        if(salesMembers.getRank() == SalesMemberType.MANAGER){
            if(salesMembers.getTeam() != null){
                return scheduleRepository.findAllTeamSchedule(
                                salesMembers.getTeam().getTeamManagerCode()
                        )
                        .stream()
                        .map(typeChange::ScheduleToScheduleResDto)
                        .toList();
            }
        }

        if(salesMembers.getTeam() == null){
            return scheduleRepository.findAllBySalesMembersAndScheduleDel_YN(salesMembers, "N")
                    .stream()
                    .map(typeChange::ScheduleToScheduleResDto)
                    .toList();
        }

        return scheduleRepository.findAllFPSchedule(salesMembers , salesMembers.getTeam().getTeamManagerCode())
                .stream()
                .map(typeChange::ScheduleToScheduleResDto)
                .toList();
    }
}
