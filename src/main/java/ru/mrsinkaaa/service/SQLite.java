package ru.mrsinkaaa.service;

import java.sql.*;

public class SQLite {

    private static final String driverName = "org.sqlite.JDBC";
    private static final String relativePath = "C:/Users/MrSinkaaa/IdeaProjects/CurrencyExchange/src/main/resources/exchange";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if(connection == null) {

            try {
                Class.forName(driverName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + relativePath);
        }
        return connection;
    }

}
