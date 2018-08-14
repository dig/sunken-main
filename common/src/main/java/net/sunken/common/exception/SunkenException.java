package net.sunken.common.exception;

public class SunkenException extends Exception {

    public SunkenException(String message) {
        super(message);
    }

    public SunkenException(String message, Throwable cause) {
        super(message, cause);
    }
}
