package com.alioth.server.domain.schedule.domain;

import com.alioth.server.common.domain.BaseEntity;
import com.alioth.server.domain.member.domain.SalesMembers;
import com.alioth.server.domain.schedule.dto.req.ScheduleReqDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @Column(nullable = false)
    private LocalDateTime scheduleStartTime;

    private LocalDateTime scheduleEndTime;

    @Column(nullable = false)
    private String scheduleNote;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ScheduleType scheduleType;

    @Column(nullable = false)
    private String allDay;

    @Builder.Default
    private String scheduleDel_YN = "N";

    @ManyToOne
    @JoinColumn(name = "SM_id")
    private SalesMembers salesMembers;

    public void delete(){
        this.scheduleDel_YN = "Y";
    }

    public void update(ScheduleReqDto scheduleUpdateDto){
        if(scheduleUpdateDto.scheduleStartTime() != null){
            this.scheduleStartTime = scheduleUpdateDto.scheduleStartTime();
        }
        if(scheduleUpdateDto.scheduleEndTime() != null){
            this.scheduleEndTime = scheduleUpdateDto.scheduleEndTime();
        }
        if(scheduleUpdateDto.scheduleNote() != null){
            this.scheduleNote = scheduleUpdateDto.scheduleNote();
        }
        if(scheduleUpdateDto.scheduleType() != null){
            this.scheduleType = scheduleUpdateDto.scheduleType();
        }
        if(scheduleUpdateDto.allDay() != null){
            this.allDay = scheduleUpdateDto.allDay();
        }
    }
}


