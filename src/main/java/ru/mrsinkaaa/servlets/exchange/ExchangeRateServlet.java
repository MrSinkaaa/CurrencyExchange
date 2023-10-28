package ru.mrsinkaaa.servlets.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.mrsinkaaa.dto.ExchangeRateDTO;
import ru.mrsinkaaa.exceptions.EmptyFormFieldException;
import ru.mrsinkaaa.exceptions.InvalidInputException;
import ru.mrsinkaaa.exceptions.exchange.ExchangeRatesNotFoundException;
import ru.mrsinkaaa.service.ExchangeRatesService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRatesService exchangeRatesService = new ExchangeRatesService();


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String method = req.getMethod();
        if (method.equals("PATCH")) {
            this.doPatch(req, resp);
        } else if(method.equals("GET")){
            this.doGet(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String codes = req.getPathInfo().replaceFirst("/", "").toUpperCase();
        String baseCode = codes.substring(0, 3);
        String targetCode = codes.substring(3);

        try {
            BufferedReader br = req.getReader();
            String line = br.readLine();

            String rate = line.split("=")[1];

            exchangeRatesService.update(baseCode, targetCode, rate);
        } catch (EmptyFormFieldException | ExchangeRatesNotFoundException | InvalidInputException e) {
            resp.sendError(e.getError().getStatus(), e.getError().getMessage());
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database unavailable");
        }

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency code. Use /exchangeRate/EURRUB");
            return;
        }

        String codes = req.getPathInfo().replaceFirst("/", "").toUpperCase();
        String baseCode = codes.substring(0, 3);
        String targetCode = codes.substring(3);

        try {
            ExchangeRateDTO exchangeRate = exchangeRatesService.findByCodes(baseCode, targetCode);

            resp.getWriter().println(new ObjectMapper().writeValueAsString(exchangeRate));
        } catch (EmptyFormFieldException | ExchangeRatesNotFoundException e) {
            resp.sendError(e.getError().getStatus(), e.getError().getMessage());
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database unavailable");
        }


    }
}
