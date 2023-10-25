package ru.mrsinkaaa.servlets.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.mrsinkaaa.entity.ExchangeRate;
import ru.mrsinkaaa.repositories.ExchangeRatesRepository;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRatesRepository exchangeRatesRepository = new ExchangeRatesRepository();


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String method = req.getMethod();
        if (method.equals("PATCH")) {
            this.doPatch(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String codes = req.getPathInfo().replaceFirst("/", "").toUpperCase();
        String baseCode = codes.substring(0, 3);
        String targetCode = codes.substring(3);

        Optional<ExchangeRate> exchangeRate = exchangeRatesRepository.findByCodes(baseCode, targetCode);

        try {
            BufferedReader br = req.getReader();
            String line = br.readLine();

            Double rate = Double.valueOf(line.split("=")[1]);

            exchangeRate.get().setRate(rate);
            exchangeRatesRepository.update(exchangeRate.get());
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON.");

        }

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency code. Use /currency/EURRUB");
            return;
        }

        String codes = req.getPathInfo().replaceFirst("/", "").toUpperCase();
        String baseCode = codes.substring(0, 3);
        String targetCode = codes.substring(3);

        Optional<ExchangeRate> exchangeRate = exchangeRatesRepository.findByCodes(baseCode, targetCode);
        if (exchangeRate.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Currency not found. Use /currency/EURRUB");
            return;
        }

        resp.getWriter().println(new ObjectMapper().writeValueAsString(exchangeRate.get()));
    }
}
