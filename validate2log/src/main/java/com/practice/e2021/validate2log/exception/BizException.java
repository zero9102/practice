package com.practice.e2021.validate2log.exception;

import lombok.Data;

@Data
public class BizException extends RuntimeException{

    private int code;
    private String message;
}
