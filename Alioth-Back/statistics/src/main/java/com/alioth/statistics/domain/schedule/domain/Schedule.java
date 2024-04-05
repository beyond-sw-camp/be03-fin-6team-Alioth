package com.alioth.statistics.domain.schedule.domain;

import com.alioth.statistics.common.domain.BaseEntity;
import com.alioth.statistics.domain.member.domain.SalesMembers;
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

}


