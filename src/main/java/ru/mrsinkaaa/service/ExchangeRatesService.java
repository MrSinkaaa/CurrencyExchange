package ru.mrsinkaaa.service;

import ru.mrsinkaaa.entity.Currency;
import ru.mrsinkaaa.entity.ExchangeRate;
import ru.mrsinkaaa.exceptions.EmptyFormFieldException;
import ru.mrsinkaaa.exceptions.InvalidInputException;
import ru.mrsinkaaa.exceptions.currency.CurrencyNotFoundException;
import ru.mrsinkaaa.exceptions.exchange.ExchangeRatesAlreadyExistException;
import ru.mrsinkaaa.exceptions.exchange.ExchangeRatesNotFoundException;
import ru.mrsinkaaa.repositories.ExchangeRatesRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesService {

    private final ExchangeRatesRepository exchangeRatesRepository = new ExchangeRatesRepository();
    private final CurrenciesService currenciesService = new CurrenciesService();

    public ExchangeRate findByCodes(String baseCurrencyCode, String targetCurrencyCode) throws SQLException, EmptyFormFieldException, ExchangeRatesNotFoundException {
        if(baseCurrencyCode.isEmpty() || targetCurrencyCode.isEmpty()) {
            throw new EmptyFormFieldException();
        }

        Optional<ExchangeRate> exchangeRate = exchangeRatesRepository.findByCodes(baseCurrencyCode, targetCurrencyCode);
        if(exchangeRate.isPresent()) {
            return exchangeRate.get();
        } else {
            throw new ExchangeRatesNotFoundException();
        }
    }

    public List<ExchangeRate> findByCode(String baseCurrencyCode) throws SQLException, EmptyFormFieldException, ExchangeRatesNotFoundException {
        if(baseCurrencyCode.isEmpty()) {
            throw new EmptyFormFieldException();
        }

        List<ExchangeRate> exchangeRates = exchangeRatesRepository.findByCode(baseCurrencyCode);
        if(!exchangeRates.isEmpty()) {
            return exchangeRates;
        } else {
            throw new ExchangeRatesNotFoundException();
        }
    }

    public List<ExchangeRate> findAll() throws SQLException {
        List<ExchangeRate> exchangeRates = exchangeRatesRepository.findAll();
        if(!exchangeRates.isEmpty()) {
            return exchangeRates;
        } else {
            throw new ExchangeRatesNotFoundException();
        }
    }

    public void save(String baseCurrencyCode, String targetCurrencyCode, String rates) throws ExchangeRatesAlreadyExistException, EmptyFormFieldException, CurrencyNotFoundException, InvalidInputException, SQLException {
        ValidationUtils.validateExchangeRate(baseCurrencyCode, targetCurrencyCode, rates);

        Currency baseCurrency = currenciesService.findByCode(baseCurrencyCode);
        Currency targetCurrency = currenciesService.findByCode(targetCurrencyCode);
        double rate = Double.parseDouble(rates);

        ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, rate);

        try {
            exchangeRatesRepository.save(exchangeRate);
        } catch (SQLException e) {
            throw new ExchangeRatesAlreadyExistException();
        }
    }

    public void update(String baseCurrencyCode, String targetCurrencyCode, String rates) throws SQLException, EmptyFormFieldException, InvalidInputException, ExchangeRatesNotFoundException {
        ValidationUtils.validateExchangeRate(baseCurrencyCode, targetCurrencyCode, rates);

        Optional<ExchangeRate> exchangeRate = exchangeRatesRepository.findByCodes(baseCurrencyCode, targetCurrencyCode);
        if(exchangeRate.isEmpty()) {
            throw new ExchangeRatesNotFoundException();
        }

        double rate = Double.parseDouble(rates);
        exchangeRate.get().setRate(rate);

        exchangeRatesRepository.update(exchangeRate.get());
    }
}
