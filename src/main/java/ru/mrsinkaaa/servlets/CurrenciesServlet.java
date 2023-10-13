package ru.mrsinkaaa.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import ru.mrsinkaaa.dto.CurrencyDTO;
import ru.mrsinkaaa.entity.Currency;
import ru.mrsinkaaa.repositories.CurrencyRepository;
import ru.mrsinkaaa.service.CurrencyService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;


@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private CurrencyRepository currencyRepository = new CurrencyRepository();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.addHeader("Access-Control-Allow-Origin", "*");

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
            JSONObject jsonObject = HTTP.toJSONObject(sb.toString());

            String code = jsonObject.getString("code");
            String fullName = jsonObject.getString("name");
            String  sign = jsonObject.getString("sign");

            currencyRepository.save(new Currency(code, fullName, sign));

        } catch (JSONException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error parsing JSON request string.");
            e.printStackTrace();
        }


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        try {
            resp.getWriter().println(new ObjectMapper().writeValueAsString(currencyRepository.findAll()));
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database unavailable.");
        }
    }

}
