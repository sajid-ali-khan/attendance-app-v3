package dev.sajid.backend.exceptions;

public class CsvValidationException extends RuntimeException{
    public CsvValidationException(String message){
        super(message);
    }

    public CsvValidationException(String message, Throwable cause){
        super(message, cause);
    }
}
