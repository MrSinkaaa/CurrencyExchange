package ru.mrsinkaaa.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class ServletUtils {

    public static StringBuilder readRequestBody(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        String line = null;

        try {
            BufferedReader reader = req.getReader();
            while((line = reader.readLine()) != null) {
                requestBody.append(line);
            }

        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid JSON.");
        }

        return requestBody;
    }
}
