package com.example.videogamereviewservice.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.core.AuthenticationException;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionHandle {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handlerNotFoundException(NotFoundException ex){
        log.warn("Not found: {}", ex.getMessage());
        return new ErrorDto(404, HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation failed!", ex);

        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ErrorDto(
                400,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed: " + errorMessage
        );
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
    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto handlerAuthorizationDenied(AuthorizationDeniedException ex){
        log.warn("Access authorization denied! {}", ex.getMessage());
        return new ErrorDto(403, HttpStatus.FORBIDDEN.getReasonPhrase(),"Access denied: " + ex.getMessage());
    }
    @ExceptionHandler(AccessDeniedException.class) //для проверки ролей (вариант старых версий), нужно покрыть тестами
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto handlerAccessDenied(AccessDeniedException ex){
        log.warn("Access denied! {}", ex.getMessage());
        return new ErrorDto(403, HttpStatus.FORBIDDEN.getReasonPhrase(),"Access denied!");
    }
    @ExceptionHandler(AuthenticationException.class) //для проверки ролей, нужно покрыть тестами
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDto handlerAuthenticationException(AuthenticationException ex){
        log.error("Authentication failed!", ex);
        return new ErrorDto(403, HttpStatus.FORBIDDEN.getReasonPhrase(),"Authentication failed! " + ex.getMessage());
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handlerGenericException(Exception ex){
        log.error("Internal Server Error!", ex);
        return new ErrorDto(500, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),"An error occurred " + ex.getMessage());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleDuplicateKeyException(DataIntegrityViolationException ex) {
        log.error("Duplicate key violation!", ex);

        String message = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

        if (message != null && message.contains("uk_users_email")) {
            return new ErrorDto(
                    409,
                    HttpStatus.CONFLICT.getReasonPhrase(),
                    "Email already exists"
            );
        } else if (message != null && message.contains("uk_users_username")) {
            return new ErrorDto(
                    409,
                    HttpStatus.CONFLICT.getReasonPhrase(),
                    "Username already exists"
            );
        }

        return new ErrorDto(
                409,
                HttpStatus.CONFLICT.getReasonPhrase(),
                "Resource already exists"
        );
    }
}
