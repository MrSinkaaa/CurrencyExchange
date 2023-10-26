package ru.mrsinkaaa.servlets.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import ru.mrsinkaaa.dto.CurrencyDTO;
import ru.mrsinkaaa.entity.Currency;
import ru.mrsinkaaa.exceptions.DatabaseUnavailableException;
import ru.mrsinkaaa.exceptions.EmptyFormFieldException;
import ru.mrsinkaaa.exceptions.InvalidInputException;
import ru.mrsinkaaa.exceptions.currency.CurrencyAlreadyExistException;
import ru.mrsinkaaa.repositories.CurrencyRepository;
import ru.mrsinkaaa.service.CurrenciesService;
import ru.mrsinkaaa.service.Utils.ServletUtils;

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

        StringBuilder requestBody = ServletUtils.readRequestBody(req, resp);

        try {
            ArrayList<String> list = new ArrayList<>(List.of(requestBody.toString().split("&")));

            String code = list.get(0).split("=")[1];
            String fullName = list.get(1).split("=")[1];
            String sign = list.get(2).split("=")[1];

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
