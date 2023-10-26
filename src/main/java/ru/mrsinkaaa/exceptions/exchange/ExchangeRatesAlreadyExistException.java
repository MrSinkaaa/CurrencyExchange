package ru.mrsinkaaa.exceptions.exchange;

import ru.mrsinkaaa.exceptions.ApplicationException;
import ru.mrsinkaaa.exceptions.ErrorMessage;

public class ExchangeRatesAlreadyExistException extends ApplicationException {

    public ExchangeRatesAlreadyExistException() {
        super(ErrorMessage.EXCHANGE_RATE_ALREADY_EXISTS);
    }
}
