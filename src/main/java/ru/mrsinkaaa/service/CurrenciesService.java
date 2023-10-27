package ru.mrsinkaaa.service;

import ru.mrsinkaaa.entity.Currency;
import ru.mrsinkaaa.exceptions.currency.CurrencyAlreadyExistException;
import ru.mrsinkaaa.exceptions.currency.CurrencyNotFoundException;
import ru.mrsinkaaa.exceptions.EmptyFormFieldException;
import ru.mrsinkaaa.exceptions.InvalidInputException;
import ru.mrsinkaaa.repositories.CurrencyRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CurrenciesService {

    private final CurrencyRepository currencyRepository = new CurrencyRepository();

    public Currency findById(Long id) throws SQLException {

        Optional<Currency> currency = currencyRepository.findById(id);
        if(currency.isPresent()) {
            return currency.get();
        } else {
            throw new CurrencyNotFoundException();
        }
    }
    public Currency findByCode(String code) throws InvalidInputException, CurrencyNotFoundException, EmptyFormFieldException, SQLException {
        if(code.isEmpty()) {
            throw new EmptyFormFieldException();
        } else if(code.length() != 3) {
            throw new InvalidInputException();
        }

        Optional<Currency> currency = currencyRepository.findByCode(code);
        if(currency.isPresent()) {
            return currency.get();
        } else {
            throw new CurrencyNotFoundException();
        }
    }

    public List<Currency> findAll() throws SQLException {
        return currencyRepository.findAll();
    }

    public void save(Currency currency) throws CurrencyAlreadyExistException, InvalidInputException, EmptyFormFieldException, SQLException {
        ValidationUtils.validateCurrency(currency.getCode(), currency.getFullName(), currency.getSign());

        if(currencyRepository.findByCode(currency.getCode()).isPresent()) {
            throw new CurrencyAlreadyExistException();
        }
        currencyRepository.save(currency);
    }
}
