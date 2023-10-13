package ru.mrsinkaaa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.mrsinkaaa.entity.Currency;
import ru.mrsinkaaa.repositories.CurrencyRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CurrencyService {

    private CurrencyRepository currencyRepository = new CurrencyRepository();

    public void findAll(HttpServletResponse resp) {
        resp.setContentType("application/json");

        try {
            resp.getWriter().println(new ObjectMapper().writeValueAsString(currencyRepository.findAll()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void findById(Long index, HttpServletResponse resp) {

        Currency currency = currencyRepository.findById(index).get();
        try {
            resp.getWriter().println(new ObjectMapper().writeValueAsString(currency));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void findByCode(String code, HttpServletResponse resp) {
        resp.setContentType("application/json");

        Currency currency = currencyRepository.findByCode(code).get();
        try {
            resp.getWriter().println(new ObjectMapper().writeValueAsString(currency));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
