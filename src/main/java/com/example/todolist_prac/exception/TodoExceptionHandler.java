package com.example.todolist_prac.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

import static com.example.todolist_prac.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class TodoExceptionHandler extends ResponseEntityExceptionHandler {

    // handle sepecific exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            WebRequest webRequest){

        log.error("ResourceNotFoundException: ", exception);

        ErrorDetails errorDetails = ErrorDetails.builder()
                .date(LocalDateTime.now())
                .message(exception.getMessage())
                .description(webRequest.getDescription(false))
                .errorCode(exception.getErrorCode())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorDetails> handleBlogAPIException(
            APIException exception,
            WebRequest webRequest){

        log.error("APIException: ", exception);

        ErrorDetails errorDetails = ErrorDetails.builder()
                .date(LocalDateTime.now())
                .message(exception.getMessage())
                .description(webRequest.getDescription(false))
                .errorCode(exception.getErrorCode())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // global exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(
            Exception exception,
            WebRequest webRequest){

        log.error("Exception ", exception);
        ErrorDetails errorDetails = ErrorDetails.builder()
                .date(LocalDateTime.now())
                .message(exception.getMessage())
                .description(webRequest.getDescription(false))
                .errorCode(INTERNAL_SERVER_ERROR)
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
