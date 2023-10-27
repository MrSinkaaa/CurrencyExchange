package ru.mrsinkaaa.servlets.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.mrsinkaaa.dto.CurrencyDTO;
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
import java.util.ArrayList;
import java.util.List;


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
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database unavailable");

        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        List<CurrencyDTO> currencies = new ArrayList<>();
        try {
            for (Currency currency : currenciesService.findAll()) {
                currencies.add(new CurrencyDTO(currency.getCode(), currency.getFullName(), currency.getSign()));
            }
            resp.getWriter().println(new ObjectMapper().writeValueAsString(currencies));
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database unavailable");
        }

    }

}
