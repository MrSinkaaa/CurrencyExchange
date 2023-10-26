package ru.mrsinkaaa.exceptions;

public class DatabaseUnavailableException extends ApplicationException {
    public DatabaseUnavailableException() {
        super(ErrorMessage.DATABASE_UNAVAILABLE);
    }
}
