package com.alioth.server.common.response;

import com.alioth.server.domain.answer.dto.res.AnswerResDto;
import com.alioth.server.domain.board.dto.res.BoardResDto;
import com.alioth.server.domain.contract.dto.res.ContractResDto;
import com.alioth.server.domain.dummy.dto.res.SimpleListDTO;
import com.alioth.server.domain.login.dto.res.LoginResDto;
import com.alioth.server.domain.member.dto.res.SalesMemberResDto;
import com.alioth.server.domain.notification.dto.res.NotificationResDto;
import com.alioth.server.domain.schedule.dto.req.ScheduleReqDto;
import com.alioth.server.domain.team.dto.TeamResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@AllArgsConstructor
public class CommonResponse {
    private HttpStatus httpStatus;
    private String message;

    @Schema(description = "반환 오브젝트", anyOf = {
            AnswerResDto.class,
            BoardResDto.class,
            ContractResDto.class,
            SimpleListDTO.class,
            LoginResDto.class,
            SalesMemberResDto.class,
            NotificationResDto.class,
            ScheduleReqDto.class,
            TeamResDto.class
    })
    private Object result;


    public static ResponseEntity<CommonResponse> responseMessage(HttpStatus status, String message, Object object) {
        return new ResponseEntity<>(
                CommonResponse.builder()
                        .httpStatus(status)
                        .message(message)
                        .result(object)
                        .build()
                ,
                status
        );
    }
    public static ResponseEntity<CommonResponse> responseMessage(HttpStatus status, String message){
        return new ResponseEntity<>(
                CommonResponse.builder()
                        .httpStatus(status)
                        .message(message)
                        .build()
                ,
                status
        );
    }
}
