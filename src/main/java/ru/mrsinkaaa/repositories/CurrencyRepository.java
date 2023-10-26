package ru.mrsinkaaa.repositories;

import ru.mrsinkaaa.dto.CurrencyDTO;
import ru.mrsinkaaa.service.SQLite;
import ru.mrsinkaaa.entity.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyRepository implements CrudRepository<Currency> {

    public CurrencyRepository() {
    }

    @Override
    public Optional<Currency> findById(Long id) throws SQLException {
        final String query = "SELECT * FROM currencies WHERE id = ?";

        PreparedStatement statement = SQLite.getConnection().prepareStatement(query);
        statement.setLong(1, id);
        statement.execute();

        ResultSet resultSet = statement.getResultSet();
        if (resultSet.next()) {
            return Optional.ofNullable(createCurrency(resultSet));
        }

        return Optional.empty();
    }

    public Optional<Currency> findByCode(String code) throws SQLException {
        final String query = "SELECT * FROM currencies WHERE code = ?";

        PreparedStatement statement = SQLite.getConnection().prepareStatement(query);
        statement.setString(1, code);
        statement.execute();

        ResultSet resultSet = statement.getResultSet();
        if (resultSet.next()) {
            return Optional.ofNullable(createCurrency(resultSet));
        }

        return Optional.empty();
    }


    @Override
    public List<Currency> findAll() throws SQLException {
        final String query = "SELECT * FROM currencies";

        PreparedStatement statement = SQLite.getConnection().prepareStatement(query);

        statement.execute();

        List<Currency> currencies = new ArrayList<>();

        ResultSet resultSet = statement.getResultSet();
        while (resultSet.next()) {
            currencies.add(createCurrency(resultSet));
        }

        return currencies;
    }

    @Override
    public void save(Currency entity) throws SQLException {
        final String query = "INSERT INTO currencies (code, fullName, sign) VALUES (?,?,?)";

        PreparedStatement statement = SQLite.getConnection().prepareStatement(query);

        statement.setString(1, entity.getCode());
        statement.setString(2, entity.getFullName());
        statement.setString(3, entity.getSign());

        statement.execute();
    }

    @Override
    public void update(Currency entity) throws SQLException {
        final String query = "UPDATE currencies SET code =?, FullName =?, sign =? WHERE id =?";

        PreparedStatement statement = SQLite.getConnection().prepareStatement(query);
        statement.setString(1, entity.getCode());
        statement.setString(2, entity.getFullName());
        statement.setString(3, entity.getSign());
        statement.setLong(4, entity.getId());

        statement.execute();

    }

    @Override
    public void delete(Long id) throws SQLException {
        final String query = "DELETE FROM currencies WHERE id =?";

        PreparedStatement statement = SQLite.getConnection().prepareStatement(query);
        statement.setLong(1, id);

        statement.execute();
    }

    private Currency createCurrency(ResultSet resultSet) {
        try {
            return new Currency(
                    resultSet.getLong("id"),
                    resultSet.getString("code"),
                    resultSet.getString("fullname"),
                    resultSet.getString("sign")
            );
        } catch (SQLException e) {
            return null;
        }
    }
}
