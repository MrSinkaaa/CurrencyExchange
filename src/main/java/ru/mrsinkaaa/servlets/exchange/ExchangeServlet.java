package ru.mrsinkaaa.servlets.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import ru.mrsinkaaa.dto.ExchangeDTO;
import ru.mrsinkaaa.dto.ExchangeRateDTO;
import ru.mrsinkaaa.entity.ExchangeRate;
import ru.mrsinkaaa.exceptions.EmptyFormFieldException;
import ru.mrsinkaaa.exceptions.InvalidInputException;
import ru.mrsinkaaa.exceptions.exchange.ExchangeRatesNotFoundException;
import ru.mrsinkaaa.service.ExchangeRatesService;
import ru.mrsinkaaa.service.ValidationUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String baseCode = req.getParameter("from");
        String targetCode = req.getParameter("to");
        String amount = req.getParameter("amount");

        ExchangeRateDTO exchangeRate;
        try {
            ValidationUtils.validateExchangeRate(baseCode, targetCode, amount);
            double parsedAmount = Double.parseDouble(amount);

            if(baseCode.equals(targetCode)) {
                throw new ExchangeRatesNotFoundException();
            }

            try {
                exchangeRate = exchangeRatesService.findByCodes(baseCode, targetCode);

                sendResponse(resp, convertExchangeRates(exchangeRate, parsedAmount));
            } catch (ExchangeRatesNotFoundException e) {

                try {
                    exchangeRate = exchangeRatesService.findByCodes(targetCode, baseCode);

                    sendResponse(resp, convertReversedExchangeRates(exchangeRate, parsedAmount));
                } catch (ExchangeRatesNotFoundException error) {
                    sendResponse(resp, convertCrossExchangeRates(baseCode, targetCode, parsedAmount));
                }
            }

        } catch (InvalidInputException | EmptyFormFieldException | ExchangeRatesNotFoundException e) {
            resp.sendError(e.getError().getStatus(), e.getError().getMessage());
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database unavailable");
        }
    }

    private ExchangeDTO convertCrossExchangeRates(String baseCode, String targetCode, double amount) throws SQLException {
        List<ExchangeRate> exchangeRatesOfBaseCode;
        List<ExchangeRateDTO> allRates = exchangeRatesService.findAll();

        ExchangeRateDTO generalRates;
        try {
            exchangeRatesOfBaseCode = exchangeRatesService.findByCode(baseCode);

            generalRates = getGeneralRates(exchangeRatesOfBaseCode, allRates, targetCode);

            double generalAmount = convertExchangeRates(generalRates, amount).getConvertedAmount();

            ExchangeRateDTO generalToTarget = exchangeRatesService.findByCodes(generalRates.getTargetCurrency().getCode(), targetCode);
            return convertExchangeRates(generalToTarget, generalAmount);
        } catch (ExchangeRatesNotFoundException e) {
            exchangeRatesOfBaseCode = exchangeRatesService.findByCode(targetCode);

            generalRates = getGeneralRates(exchangeRatesOfBaseCode, allRates, baseCode);

            double generalAmount = convertReversedExchangeRates(generalRates, amount).getConvertedAmount();

            ExchangeRateDTO generalToTarget = exchangeRatesService.findByCodes(generalRates.getTargetCurrency().getCode(), baseCode);
            return convertReversedExchangeRates(generalToTarget, generalAmount);
        }
    }

    private ExchangeRateDTO getGeneralRates(List<ExchangeRate> exchangeRatesOfBaseCode, List<ExchangeRateDTO> allRates, String targetCode) {
        ExchangeRateDTO generalRates = null;
        for (ExchangeRate base : exchangeRatesOfBaseCode) {
            for (ExchangeRateDTO all : allRates) {
                if (base.getTargetCurrency().getCode().equals(all.getBaseCurrency().getCode()) && all.getTargetCurrency().getCode().equals(targetCode)) {
                    generalRates = new ModelMapper().map(base, ExchangeRateDTO.class);
                    break;
                }
            }
        }
        return generalRates;
    }

    private ExchangeDTO convertReversedExchangeRates(ExchangeRateDTO exchangeRate, double amount) {
        double convertedAmount = toFixed(amount / exchangeRate.getRate());

        return new ExchangeDTO(
                exchangeRate.getTargetCurrency(),
                exchangeRate.getBaseCurrency(),
                exchangeRate.getRate(),
                amount,
                convertedAmount);
    }

    private ExchangeDTO convertExchangeRates(ExchangeRateDTO exchangeRate, double amount) {
        double convertedAmount = toFixed(exchangeRate.getRate() * amount);

        return new ExchangeDTO(
                exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),
                exchangeRate.getRate(),
                amount,
                convertedAmount);
    }

    private double toFixed(double number) {
        double scale = Math.pow(10, 2);
        return Math.round(number * scale) / scale;
    }

    private void sendResponse(HttpServletResponse resp, ExchangeDTO exchangeDTO) throws IOException {
        resp.getWriter().println(new ObjectMapper().writeValueAsString(exchangeDTO));
    }
}
