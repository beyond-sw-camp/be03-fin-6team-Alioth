package com.alioth.server.domain.schedule.dto.res;

import com.alioth.server.domain.schedule.domain.ScheduleType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
public record ScheduleResDto(
         Long scheduleId,
         LocalDateTime scheduleStartTime,
         LocalDateTime scheduleEndTime,
         String scheduleTitle,
         String scheduleNote,
         ScheduleType scheduleType,
         String share,
         String allDay,
         String del_yn,
         Long memberId,
         String color
){}


