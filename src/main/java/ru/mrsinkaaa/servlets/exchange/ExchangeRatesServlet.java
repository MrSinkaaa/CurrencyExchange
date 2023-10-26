package ru.mrsinkaaa.servlets.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import ru.mrsinkaaa.entity.Currency;
import ru.mrsinkaaa.entity.ExchangeRate;
import ru.mrsinkaaa.exceptions.EmptyFormFieldException;
import ru.mrsinkaaa.exceptions.InvalidInputException;
import ru.mrsinkaaa.exceptions.currency.CurrencyNotFoundException;
import ru.mrsinkaaa.exceptions.exchange.ExchangeRatesAlreadyExistException;
import ru.mrsinkaaa.repositories.CurrencyRepository;
import ru.mrsinkaaa.repositories.ExchangeRatesRepository;
import ru.mrsinkaaa.service.CurrenciesService;
import ru.mrsinkaaa.service.ExchangeRatesService;
import ru.mrsinkaaa.service.Utils.ServletUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder requestBody = ServletUtils.readRequestBody(req, resp);

        try {
            ArrayList<String> list = new ArrayList<>(List.of(requestBody.toString().split("&")));

            String baseCode = list.get(0).split("=")[1];
            String targetCode = list.get(1).split("=")[1];
            String rate = list.get(2).split("=")[1];

            exchangeRatesService.save(baseCode, targetCode, rate);

        } catch (InvalidInputException | ExchangeRatesAlreadyExistException | CurrencyNotFoundException | EmptyFormFieldException e) {
           resp.sendError(e.getError().getStatus(), e.getError().getMessage());
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database unavailable");
        }
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.getWriter().println(new ObjectMapper().writeValueAsString(exchangeRatesService.findAll()));
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database unavailable");
        }
    }


}
