package com.alioth.server.domain.excel.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
public record ExcelReqDto(
  LocalDateTime startDate,
  LocalDateTime endDate

) {}
