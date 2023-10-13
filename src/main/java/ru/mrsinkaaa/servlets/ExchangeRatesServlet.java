package ru.mrsinkaaa.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.mrsinkaaa.repositories.ExchangeRatesRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private ExchangeRatesRepository exchangeRatesRepository = new ExchangeRatesRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        resp.getWriter().println(new ObjectMapper().writeValueAsString(exchangeRatesRepository.findAll()));
    }


}
