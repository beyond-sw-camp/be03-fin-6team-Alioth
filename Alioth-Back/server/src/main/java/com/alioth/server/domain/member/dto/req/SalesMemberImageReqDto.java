package com.alioth.server.domain.member.dto.req;

import org.springframework.web.multipart.MultipartFile;

public record SalesMemberImageReqDto(
    MultipartFile memberImage
) {
}
