package ru.mrsinkaaa.repositories;

import ru.mrsinkaaa.entity.ExchangeRate;
import ru.mrsinkaaa.service.SQLite;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesRepository implements CrudRepository<ExchangeRate> {
    private final CurrencyRepository currencyRepository = new CurrencyRepository();

    @Override
    public Optional<ExchangeRate> findById(Long id) throws SQLException {
        final String query = "SELECT * FROM exchangeRates WHERE id = ?";

        PreparedStatement statement = SQLite.getConnection().prepareStatement(query);
        statement.setLong(1, id);

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return Optional.ofNullable(createExchangeRate(resultSet));
        }

        return Optional.empty();
    }

    public List<ExchangeRate> findByCode(String baseCurrencyCode) throws SQLException {
        final String query = "SELECT * FROM exchangeRates WHERE BaseCurrencyId =?";

        PreparedStatement statement = SQLite.getConnection().prepareStatement(query);
        statement.setLong(1, currencyRepository.findByCode(baseCurrencyCode).get().getId());

        ResultSet resultSet = statement.executeQuery();
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        while (resultSet.next()) {
            exchangeRates.add(createExchangeRate(resultSet));
        }

        return exchangeRates;
    }

    public Optional<ExchangeRate> findByCodes(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        final String query = "SELECT * FROM exchangeRates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";

        PreparedStatement statement = SQLite.getConnection().prepareStatement(query);
        statement.setLong(1, currencyRepository.findByCode(baseCurrencyCode).get().getId());
        statement.setLong(2, currencyRepository.findByCode(targetCurrencyCode).get().getId());

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return Optional.ofNullable(createExchangeRate(resultSet));
        }

        return Optional.empty();
    }

    @Override
    public List<ExchangeRate> findAll() throws SQLException {
        final String query = "SELECT * FROM exchangeRates";

        PreparedStatement statement = SQLite.getConnection().prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();

        List<ExchangeRate> exchangeRates = new ArrayList<>();
        while (resultSet.next()) {
            exchangeRates.add(createExchangeRate(resultSet));
        }

        return exchangeRates;
    }

    @Override
    public void save(ExchangeRate entity) throws SQLException {
        final String query = "INSERT INTO exchangeRates (BaseCurrencyId, TargetCurrencyId, rate) VALUES (?,?,?)";

        PreparedStatement statement = SQLite.getConnection().prepareStatement(query);

        statement.setLong(1, entity.getBaseCurrency().getId());
        statement.setLong(2, entity.getTargetCurrency().getId());
        statement.setDouble(3, entity.getRate());
        statement.executeUpdate();

    }

    @Override
    public void update(ExchangeRate entity) throws SQLException {
        final String query = "UPDATE exchangeRates SET BaseCurrencyId =?, TargetCurrencyId =?, rate =? WHERE id =?";

        PreparedStatement statement = SQLite.getConnection().prepareStatement(query);

        statement.setLong(1, entity.getBaseCurrency().getId());
        statement.setLong(2, entity.getTargetCurrency().getId());
        statement.setDouble(3, entity.getRate());
        statement.setLong(4, entity.getId());
        statement.executeUpdate();
    }

    @Override
    public void delete(Long id) throws SQLException {
        final String query = "DELETE FROM exchangeRates WHERE id =?";

        PreparedStatement preparedStatement = SQLite.getConnection().prepareStatement(query);

        preparedStatement.setLong(1, id);
        preparedStatement.executeUpdate();
    }

    private ExchangeRate createExchangeRate(ResultSet resultSet) {
        try {
            return new ExchangeRate(
                    resultSet.getLong("id"),
                    currencyRepository.findById(resultSet.getLong("BaseCurrencyId")).get(),
                    currencyRepository.findById(resultSet.getLong("TargetCurrencyId")).get(),
                    resultSet.getDouble("rate")
            );
        } catch (SQLException e) {
            return null;
        }
    }
}
