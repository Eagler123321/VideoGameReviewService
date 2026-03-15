package com.example.videogamereviewservice.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandle {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handlerNotFoundException(NotFoundException ex){
        log.warn("Not found: {}", ex.getMessage());
        return new ErrorDto(404, HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage());
    }
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handlerValidationException(ValidationException ex){
        log.error("Validation error", ex);
        return new ErrorDto(400, HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage());
    }
    @ExceptionHandler(UnexpectedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handlerInternalException(UnexpectedException ex){
        log.error("Unexpected error", ex);
        return new ErrorDto(500, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage());
    }
    @ExceptionHandler(InvalidIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handlerInvalidIdException(InvalidIdException ex){
        log.error("Invalid id error", ex);
        return new ErrorDto(400, HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage());
    }
}
