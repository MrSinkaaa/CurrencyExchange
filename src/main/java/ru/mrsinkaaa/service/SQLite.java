package ru.mrsinkaaa.service;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;

public class SQLite {

    private static final String driverName = "org.sqlite.JDBC";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if(connection == null) {
            URL resource = SQLite.class.getClassLoader().getResource("exchange");
            String path;

            try {
                assert resource != null;
                path = new File(resource.toURI()).getAbsolutePath();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            try {
                Class.forName(driverName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
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
