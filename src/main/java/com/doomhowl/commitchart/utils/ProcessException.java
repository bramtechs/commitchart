package com.doomhowl.commitchart.utils;

public class ProcessException extends RuntimeException {
    public ProcessException(String message, Exception base) {
        super(message, base);
    }

    public ProcessException(String message) {
        super(message);
    }

    public ProcessException(Exception base) {
        super(base);
    }
}
