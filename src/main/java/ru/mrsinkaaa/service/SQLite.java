package ru.mrsinkaaa.service;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite {

    private final String driverName = "org.sqlite.JDBC";
    private Connection connection = null;

    public SQLite() {
        URL resource = SQLite.class.getClassLoader().getResource("exchange");
        String path = null;

        try {
            assert resource != null;
            path = new File(resource.toURI()).getAbsolutePath();;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        connection.close();
    }
}
