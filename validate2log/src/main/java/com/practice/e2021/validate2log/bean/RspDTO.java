package com.practice.e2021.validate2log.bean;

import lombok.Data;

@Data
public class RspDTO {

    private int code;
    private String message;
    private Object data;


    public RspDTO(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public RspDTO(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static RspDTO success() {
        return new RspDTO(200, "OK", "");
    }
}
