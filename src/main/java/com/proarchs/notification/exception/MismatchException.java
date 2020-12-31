package com.proarchs.notification.exception;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-09-20T20:27:27.686Z")

public class MismatchException extends ApiException {
    private int code;
    public MismatchException (int code, String msg) {
        super(code, msg);
        this.code = code;
    }
}
