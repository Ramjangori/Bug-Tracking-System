package com.exception;

public class DeleteUserException extends RuntimeException{
    public DeleteUserException(String message) {
        super(message);
    }
}
