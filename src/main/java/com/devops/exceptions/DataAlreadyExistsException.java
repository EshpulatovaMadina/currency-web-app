package com.devops.exceptions;

public class DataAlreadyExistsException extends RuntimeException{
    public DataAlreadyExistsException(String msg) {
        super(msg);
    }
}
