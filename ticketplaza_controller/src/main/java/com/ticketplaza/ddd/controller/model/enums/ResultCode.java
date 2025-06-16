package com.ticketplaza.ddd.controller.model.enums;

public enum ResultCode {

    /**
     * Success code
     */
    SUCCESS(200, "Thành công"),

    /**
     * Invalid param
     */
    PARAMS_ERROR(4002, "Invalid param"),

    /**
     *
     */
    ERROR(400, "Bad request"),

    /**
     * User
     */
    USER_SESSION_EXPIRED(20004, "Login session is expired, please re-login"),
    USER_PERMISSION_ERROR(20005, "PERMISSION DENIED"),
    USER_AUTH_ERROR(20005, "Authentication Failed"),

    RATE_LIMIT_ERROR(1003, "Server has reached its limit, please try again later."),
    ;


    private final Integer code;
    private final String message;


    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}
