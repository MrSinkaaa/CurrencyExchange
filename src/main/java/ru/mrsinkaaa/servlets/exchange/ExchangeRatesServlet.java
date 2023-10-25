package ru.mrsinkaaa.servlets.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import ru.mrsinkaaa.entity.Currency;
import ru.mrsinkaaa.entity.ExchangeRate;
import ru.mrsinkaaa.repositories.CurrencyRepository;
import ru.mrsinkaaa.repositories.ExchangeRatesRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private ExchangeRatesRepository exchangeRatesRepository = new ExchangeRatesRepository();
    private CurrencyRepository currencyRepository = new CurrencyRepository();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuffer sb = new StringBuffer();
        String line = null;

        try {
            BufferedReader br = req.getReader();
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid JSON.");
        }

        try {
            ArrayList<String> list = new ArrayList<>(List.of(sb.toString().split("&")));

            String baseCode = list.get(0).split("=")[1];
            String targetCode = list.get(1).split("=")[1];
            Double rate = Double.valueOf(list.get(2).split("=")[1]);

            Currency baseCurrency = currencyRepository.findByCode(baseCode).get();
            Currency targetCurrency = currencyRepository.findByCode(targetCode).get();

            exchangeRatesRepository.save(new ExchangeRate(baseCurrency, targetCurrency, rate));

        } catch (JSONException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error parsing JSON request string.");
            e.printStackTrace();
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().println(new ObjectMapper().writeValueAsString(exchangeRatesRepository.findAll()));
    }


}
