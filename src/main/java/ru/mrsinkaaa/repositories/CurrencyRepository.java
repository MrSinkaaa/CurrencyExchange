package ru.mrsinkaaa.repositories;

import ru.mrsinkaaa.dto.CurrencyDTO;
import ru.mrsinkaaa.service.SQLite;
import ru.mrsinkaaa.entity.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyRepository implements CrudRepository<Currency> {

    private final SQLite sqlite = new SQLite();

    public CurrencyRepository() {
    }

    @Override
    public Optional<Currency> findById(Long id) {
        final String query = "SELECT * FROM currencies WHERE id = ?";

        try {
            PreparedStatement statement = sqlite.getConnection().prepareStatement(query);
            statement.setLong(1, id);
            statement.execute();

            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return Optional.ofNullable(createCurrency(resultSet));
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public Optional<Currency> findByCode(String code) {
        final String query = "SELECT * FROM currencies WHERE code = ?";

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = sqlite.getConnection().prepareStatement(query);
            statement.setString(1, code);
            statement.execute();

            resultSet = statement.getResultSet();
            if (resultSet.next()) {
                return Optional.ofNullable(createCurrency(resultSet));
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            sqlite.closeConnection(statement, resultSet);
        }

        return Optional.empty();
    }


    @Override
    public List<Currency> findAll() {
        final String query = "SELECT * FROM currencies";

        try {
            PreparedStatement statement = sqlite.getConnection().prepareStatement(query);

            statement.execute();

            List<Currency> currencies = new ArrayList<>();

            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                currencies.add(createCurrency(resultSet));
            }

            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void save(Currency entity) {
        final String query = "INSERT INTO currencies (code, fullName, sign) VALUES (?,?,?)";

        try {
            PreparedStatement statement = sqlite.getConnection().prepareStatement(query);

            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getFullName());
            statement.setString(3, entity.getSign());

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Currency entity) {
        final String query = "UPDATE currencies SET code =?, FullName =?, sign =? WHERE id =?";

        try {
            PreparedStatement statement = sqlite.getConnection().prepareStatement(query);
            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getFullName());
            statement.setString(3, entity.getSign());
            statement.setLong(4, entity.getId());

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        final String query = "DELETE FROM currencies WHERE id =?";

        try {
            PreparedStatement statement = sqlite.getConnection().prepareStatement(query);
            statement.setLong(1, id);

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
