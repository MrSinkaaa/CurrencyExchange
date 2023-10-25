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
    private final ExchangeRatesRepository exchangeRatesRepository = new ExchangeRatesRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String baseCode = req.getParameter("from");
        String targetCode = req.getParameter("to");
        double amount = Double.parseDouble(req.getParameter("amount"));

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

                Optional<ExchangeRate> generalRates;
                if(exchangeRatesOfBaseCode.isEmpty()) {
                    exchangeRatesOfBaseCode = exchangeRatesRepository.findByCode(targetCode);

                    generalRates = getGeneralRates(exchangeRatesOfBaseCode, allRates, baseCode);
                    double generalAmount = convertReversedExchangeRates(generalRates, amount).getConvertedAmount();

                    Optional<ExchangeRate> generalToTarget = exchangeRatesRepository.findByCodes(generalRates.get().getTargetCurrency().getCode(), baseCode);
                    sendResponse(resp, convertReversedExchangeRates(generalToTarget, generalAmount));
                } else {
                    generalRates = getGeneralRates(exchangeRatesOfBaseCode, allRates, targetCode);

                    double generalAmount = convertExchangeRates(generalRates, amount).getConvertedAmount();

                    Optional<ExchangeRate> generalToTarget = exchangeRatesRepository.findByCodes(generalRates.get().getTargetCurrency().getCode(), targetCode);
                    sendResponse(resp, convertExchangeRates(generalToTarget, generalAmount));
                }

            }

        }
    }

    private static Optional<ExchangeRate> getGeneralRates(List<ExchangeRate> exchangeRatesOfBaseCode, List<ExchangeRate> allRates, String targetCode) {
        Optional<ExchangeRate> generalRates = Optional.empty();
        for(ExchangeRate base : exchangeRatesOfBaseCode) {
            for(ExchangeRate all : allRates) {
                if(base.getTargetCurrency().getCode().equals(all.getBaseCurrency().getCode()) && all.getTargetCurrency().getCode().equals(targetCode)) {
                    generalRates = Optional.of(base);
                    break;
                }
            }
        }
        return generalRates;
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
