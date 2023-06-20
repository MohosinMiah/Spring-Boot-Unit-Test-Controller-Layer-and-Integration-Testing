package com.testcontrollerlayer.testcontrollerlayer.exception;

public class DataDuplicationException extends RuntimeException{

    public DataDuplicationException(String message)
    {
        super(message);
    }

    public DataDuplicationException(String message, Throwable cause)
    {
        super(message,cause);
    }
}