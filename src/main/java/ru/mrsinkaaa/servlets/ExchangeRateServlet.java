package ru.mrsinkaaa.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.mrsinkaaa.entity.ExchangeRate;
import ru.mrsinkaaa.repositories.ExchangeRatesRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRatesRepository exchangeRatesRepository = new ExchangeRatesRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        if(req.getPathInfo() == null || req.getPathInfo().equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency code. Use /currency/EURRUB");
            return;
        }

        String codes = req.getPathInfo().replaceFirst("/", "").toUpperCase();
        String baseCode = codes.substring(0, 3);
         String targetCode = codes.substring(3);

        Optional<ExchangeRate> exchangeRate = exchangeRatesRepository.findByCodes(baseCode, targetCode);
        if(exchangeRate.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Currency not found. Use /currency/EURRUB");
            return;
        }

        resp.getWriter().println(new ObjectMapper().writeValueAsString(exchangeRate));
    }
}
