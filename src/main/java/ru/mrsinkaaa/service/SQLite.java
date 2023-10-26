package ru.mrsinkaaa.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public static void closeConnection(PreparedStatement statement, ResultSet resultSet) {
        if(statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void closeConnection(PreparedStatement statement) {
        if(statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
