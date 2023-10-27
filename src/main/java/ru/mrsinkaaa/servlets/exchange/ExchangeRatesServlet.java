package ru.mrsinkaaa.servlets.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.mrsinkaaa.exceptions.EmptyFormFieldException;
import ru.mrsinkaaa.exceptions.InvalidInputException;
import ru.mrsinkaaa.exceptions.currency.CurrencyNotFoundException;
import ru.mrsinkaaa.exceptions.exchange.ExchangeRatesAlreadyExistException;
import ru.mrsinkaaa.service.ExchangeRatesService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseCode = req.getParameter("baseCurrencyCode");
            String targetCode = req.getParameter("targetCurrencyCode");
            String rate = req.getParameter("rate");

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
