package com.alioth.statistics.domain.batch.dto.res;

import lombok.Builder;

@Builder
public record BatchProductPriceResDto(
    String category,
    String price
) {
}
