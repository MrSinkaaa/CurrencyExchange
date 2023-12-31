package ru.mrsinkaaa.servlets.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.mrsinkaaa.entity.Currency;
import ru.mrsinkaaa.exceptions.EmptyFormFieldException;
import ru.mrsinkaaa.exceptions.InvalidInputException;
import ru.mrsinkaaa.exceptions.currency.CurrencyAlreadyExistException;
import ru.mrsinkaaa.service.CurrenciesService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrenciesService currenciesService = new CurrenciesService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String code = req.getParameter("code").trim();
            String fullName = req.getParameter("name").trim();
            String sign = req.getParameter("sign").trim();

            currenciesService.save(new Currency(code, fullName, sign));

        } catch (CurrencyAlreadyExistException | EmptyFormFieldException | InvalidInputException e) {
            resp.sendError(e.getError().getStatus(), e.getError().getMessage());
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.getWriter().println(new ObjectMapper().writeValueAsString(currenciesService.findAll()));
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database unavailable");
        }

    }

}
