package ru.mrsinkaaa.servlets.currency;

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

    private final CurrencyRepository currencyRepository = new CurrencyRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        //Check if the path is valid
        if(req.getPathInfo() == null || req.getPathInfo().equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency code. Use /currency/EUR");
            return;
        }

        //Extract the currency code from the path
        String currencyCode = req.getPathInfo().replaceFirst("/", "").toUpperCase();

        //Retrieve currency information based on the code
        Optional<Currency> currency = currencyRepository.findByCode(currencyCode);

        //Handle the case where the currency is not found
        if(currency.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Currency not found. Use /currency/EUR");
            return;
        }

        // Convert the currency object to JSON and send it in the response
        resp.getWriter().println(new ObjectMapper().writeValueAsString(currency));
    }

}
