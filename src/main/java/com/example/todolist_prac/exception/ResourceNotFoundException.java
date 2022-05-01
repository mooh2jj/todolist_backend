package com.example.todolist_prac.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

}
