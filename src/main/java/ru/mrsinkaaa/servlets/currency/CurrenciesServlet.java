package ru.mrsinkaaa.servlets.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import ru.mrsinkaaa.dto.CurrencyDTO;
import ru.mrsinkaaa.entity.Currency;
import ru.mrsinkaaa.repositories.CurrencyRepository;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

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

            String code = list.get(0).split("=")[1];
            String fullName = list.get(1).split("=")[1];
            String  sign = list.get(2).split("=")[1];

            currencyRepository.save(new Currency(code, fullName, sign));

        } catch (JSONException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error parsing JSON request string.");
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            resp.getWriter().println(new ObjectMapper().writeValueAsString(currencyRepository.findAll().stream().map(currency -> new CurrencyDTO(currency.getCode(), currency.getFullName(), currency.getSign())).toList()));
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database unavailable.");
        }
    }

}
