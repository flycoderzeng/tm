package com.tm.worker.core.exception;

public class TMException extends RuntimeException {
    public TMException(String msg) {
        super(msg);
    }

    public TMException() {}

    public TMException(Throwable cause) {super(cause);}

    public TMException(String message, Throwable cause) {
        super(message, cause);
    }
}
