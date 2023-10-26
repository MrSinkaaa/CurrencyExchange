package ru.mrsinkaaa.exceptions;

import javax.servlet.http.HttpServletResponse;

public enum ErrorMessage {

    CURRENCY_NOT_FOUND("Currency not found", HttpServletResponse.SC_NOT_FOUND),
    EMPTY_FORM_FIELD("Required form field is missing", HttpServletResponse.SC_BAD_REQUEST),
    CURRENCY_ALREADY_EXISTS("Currency with this code already exists", HttpServletResponse.SC_CONFLICT),
    EXCHANGE_RATES_NOT_FOUND("Exchange rates not found", HttpServletResponse.SC_NOT_FOUND),
    EXCHANGE_RATE_ALREADY_EXISTS("Exchange rate with this currencies already exists", HttpServletResponse.SC_CONFLICT),
    INVALID_DATA("Input data is invalid", HttpServletResponse.SC_CONFLICT),
    DATABASE_UNAVAILABLE("Database unavailable", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

    private final String message;
    private final int status;

    ErrorMessage(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
