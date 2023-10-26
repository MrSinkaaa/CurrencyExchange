package ru.mrsinkaaa.exceptions.exchange;

import ru.mrsinkaaa.exceptions.ApplicationException;
import ru.mrsinkaaa.exceptions.ErrorMessage;

public class ExchangeRatesNotFoundException extends ApplicationException {

    public ExchangeRatesNotFoundException() {
        super(ErrorMessage.EXCHANGE_RATES_NOT_FOUND);
    }
}
