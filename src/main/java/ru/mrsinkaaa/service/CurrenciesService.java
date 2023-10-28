package ru.mrsinkaaa.service;

import org.modelmapper.ModelMapper;
import ru.mrsinkaaa.dto.CurrencyDTO;
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
    private final ModelMapper modelMapper = new ModelMapper();

    public CurrencyDTO findById(Long id) throws SQLException {

        Optional<Currency> currency = currencyRepository.findById(id);
        if(currency.isPresent()) {
            return modelMapper.map(currency.get(), CurrencyDTO.class);
        } else {
            throw new CurrencyNotFoundException();
        }
    }
    public CurrencyDTO findByCode(String code) throws InvalidInputException, CurrencyNotFoundException, EmptyFormFieldException, SQLException {
        if(code.isEmpty()) {
            throw new EmptyFormFieldException();
        } else if(code.length() != 3) {
            throw new InvalidInputException();
        }

        Optional<Currency> currency = currencyRepository.findByCode(code);
        if(currency.isPresent()) {
            return modelMapper.map(currency.get(), CurrencyDTO.class);
        } else {
            throw new CurrencyNotFoundException();
        }
    }

    public List<CurrencyDTO> findAll() throws SQLException {
        return currencyRepository.findAll().stream().map(currency ->
                modelMapper.map(currency, CurrencyDTO.class)).toList();
    }

    public void save(Currency currency) throws InvalidInputException, EmptyFormFieldException, CurrencyAlreadyExistException {
        ValidationUtils.validateCurrency(currency.getCode(), currency.getFullName(), currency.getSign());

        try {
            currencyRepository.save(currency);
        } catch (SQLException e) {
            throw new CurrencyAlreadyExistException();
        }
    }
}
