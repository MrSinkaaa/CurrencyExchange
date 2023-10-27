package ru.mrsinkaaa.servlets.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.mrsinkaaa.entity.Currency;
import ru.mrsinkaaa.exceptions.EmptyFormFieldException;
import ru.mrsinkaaa.exceptions.InvalidInputException;
import ru.mrsinkaaa.exceptions.currency.CurrencyNotFoundException;
import ru.mrsinkaaa.service.CurrenciesService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrenciesService currenciesService = new CurrenciesService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency code. Use /currency/EUR");
            return;
        }

        Currency currency = null;
        try {
            String currencyCode = req.getPathInfo().replaceFirst("/", "").toUpperCase();

            currency = currenciesService.findByCode(currencyCode);
        } catch (CurrencyNotFoundException | InvalidInputException | EmptyFormFieldException e) {
            resp.sendError(e.getError().getStatus(), e.getError().getMessage());
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database unavailable");
        }

        resp.getWriter().println(new ObjectMapper().writeValueAsString(currency));
    }

}
