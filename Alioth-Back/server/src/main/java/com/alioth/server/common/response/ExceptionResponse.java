package com.alioth.server.common.response;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ExceptionResponse {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> AllExceptionHandler(Exception e){

        if(e instanceof MethodArgumentNotValidException){
            String errorMessage = ((MethodArgumentNotValidException) e).getBindingResult()
                    .getAllErrors()
                    .get(0)
                    .getDefaultMessage();
            log.error(e.getClass().getName() + " : " + errorMessage);
            return CommonResponse.responseMessage(HttpStatus.BAD_REQUEST, errorMessage);
        }

        log.error(e.getClass().getName() + " : " + e.getMessage());

        if(e instanceof EntityNotFoundException){
            return CommonResponse.responseMessage(HttpStatus.NOT_FOUND, e.getMessage());
        }

        if(e instanceof IllegalArgumentException){
            return CommonResponse.responseMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        if(e instanceof DataIntegrityViolationException){
            return CommonResponse.responseMessage(HttpStatus.CONFLICT, e.getMessage());
        }

        if(e instanceof AccessDeniedException){
            return CommonResponse.responseMessage(HttpStatus.UNAUTHORIZED , e.getMessage());
        }

        log.error(Arrays.toString(e.getStackTrace()));

        return CommonResponse.responseMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
