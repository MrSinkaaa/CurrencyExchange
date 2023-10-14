package ru.mrsinkaaa.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.mrsinkaaa.entity.Currency;
import ru.mrsinkaaa.repositories.CurrencyRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private CurrencyRepository currencyRepository = new CurrencyRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        if(req.getPathInfo() == null || req.getPathInfo().equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency code. Use /currency/EUR");
            return;
        }

        String currencyCode = req.getPathInfo().replaceFirst("/", "").toUpperCase();

        Optional<Currency> currency = currencyRepository.findByCode(currencyCode);
        if(currency.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Currency not found. Use /currency/EUR");
            return;
        }

        resp.getWriter().println(new ObjectMapper().writeValueAsString(currency));
    }

}
