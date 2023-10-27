package ru.mrsinkaaa.service;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;

public class SQLite {

    private static final String driverName = "org.sqlite.JDBC";
    private static final String db = "exchange";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if(connection == null) {

            try {
                Class.forName(driverName);
                URL resource  = SQLite.class.getClassLoader().getResource(db);
                assert resource != null;
                String path = "jdbc:sqlite:" + new File(resource.toURI()).getAbsolutePath();
                connection = DriverManager.getConnection(path);
            } catch (ClassNotFoundException | URISyntaxException e) {
                throw new RuntimeException(e);
            }

        }
        return connection;
    }

}
