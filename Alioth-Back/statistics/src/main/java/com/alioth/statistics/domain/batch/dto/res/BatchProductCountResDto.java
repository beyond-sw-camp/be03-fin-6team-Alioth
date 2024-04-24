package com.alioth.statistics.domain.batch.dto.res;

import lombok.Builder;

@Builder
public record BatchProductCountResDto(
    String category,
    Long count
) {
}
