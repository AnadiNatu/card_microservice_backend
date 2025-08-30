package com.microservice.admin_service.exception;

public class UserNotFoundException extends RuntimeException{

    private static final long seriesVersionUID = 3;

    public UserNotFoundException(String message){super(message);}
}
