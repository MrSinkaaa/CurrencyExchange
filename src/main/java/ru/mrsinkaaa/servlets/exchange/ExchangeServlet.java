package ru.mrsinkaaa.servlets.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.mrsinkaaa.dto.ExchangeRateDTO;
import ru.mrsinkaaa.entity.ExchangeRate;
import ru.mrsinkaaa.repositories.CurrencyRepository;
import ru.mrsinkaaa.repositories.ExchangeRatesRepository;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private CurrencyRepository currencyRepository = new CurrencyRepository();
    private ExchangeRatesRepository exchangeRatesRepository = new ExchangeRatesRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");

        String baseCode = req.getParameter("from");
        String targetCode = req.getParameter("to");
        int amount = Integer.parseInt(req.getParameter("amount"));

        Optional<ExchangeRate> exchangeRate = exchangeRatesRepository.findByCodes(baseCode, targetCode);

        if(exchangeRate.isPresent()) {
            sendResponse(resp, convertExchangeRates(exchangeRate, amount));

        } else {
            exchangeRate = exchangeRatesRepository.findByCodes(targetCode, baseCode);

            if(exchangeRate.isPresent()) {
                sendResponse(resp, convertReversedExchangeRates(exchangeRate, amount));

            } else {
                List<ExchangeRate> exchangeRatesOfBaseCode = exchangeRatesRepository.findByCode(baseCode);
                List<ExchangeRate> allRates = exchangeRatesRepository.findAll();

                Optional<ExchangeRate> generalRates = null;
                for(ExchangeRate base : exchangeRatesOfBaseCode) {
                    for(ExchangeRate all : allRates) {
                        if(base.getTargetCurrency().getCode().equals(all.getBaseCurrency().getCode()) && all.getTargetCurrency().getCode().equals(targetCode)) {
                            generalRates = Optional.of(base);
                        }
                    }
                }


                double generalAmount = convertExchangeRates(generalRates, amount).getConvertedAmount();

                Optional<ExchangeRate> generalToTarget = exchangeRatesRepository.findByCodes(generalRates.get().getTargetCurrency().getCode(), targetCode);
                sendResponse(resp, convertExchangeRates(generalToTarget, generalAmount));
            }

            // много багов с переводом обратно и зацикливанние, если нет у целевой валюты другой пары в качестве base
        }
    }

    private ExchangeRateDTO convertReversedExchangeRates(Optional<ExchangeRate> exchangeRate, double amount) {
        double convertedAmount = toFixed(amount / exchangeRate.get().getRate(), 2);

        return new ExchangeRateDTO(
                exchangeRate.get().getTargetCurrency(),
                exchangeRate.get().getBaseCurrency(),
                exchangeRate.get().getRate(),
                amount,
                convertedAmount);
    }

    private ExchangeRateDTO convertExchangeRates(Optional<ExchangeRate> exchangeRate, double amount) {
        double convertedAmount = toFixed(exchangeRate.get().getRate() * amount, 2);

        return new ExchangeRateDTO(
                exchangeRate.get().getBaseCurrency(),
                exchangeRate.get().getTargetCurrency(),
                exchangeRate.get().getRate(),
                amount,
                convertedAmount);
    }

    private double toFixed(double number, int digits) {
        double scale = Math.pow(10, digits);
        return Math.round(number * scale) / scale;
    }

    private void sendResponse(HttpServletResponse resp, ExchangeRateDTO exchangeRateDTO) throws IOException {
        resp.getWriter().println(new ObjectMapper().writeValueAsString(exchangeRateDTO));
    }
}
