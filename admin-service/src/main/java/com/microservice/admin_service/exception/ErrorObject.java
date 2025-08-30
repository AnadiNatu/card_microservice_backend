package com.microservice.admin_service.exception;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorObject {

    private Integer statusCode;
    private String message;
    private Date timeStamp;
}
