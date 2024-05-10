package com.alioth.server.domain.board.dto.req;

import org.springframework.web.multipart.MultipartFile;

public record SugBoardImageReqDto(
    MultipartFile boardImage
) {
}
