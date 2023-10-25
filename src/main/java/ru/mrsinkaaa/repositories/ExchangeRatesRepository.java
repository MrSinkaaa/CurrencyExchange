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

    public ExchangeRatesRepository() {
    }

    @Override
    public Optional<ExchangeRate> findById(Long id) {
        final String query = "SELECT * FROM exchangeRates WHERE id = ?";

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = SQLite.getConnection().prepareStatement(query);
            statement.setLong(1, id);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.ofNullable(createExchangeRate(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            SQLite.closeConnection(statement, resultSet);
        }
        return Optional.empty();
    }

    public List<ExchangeRate> findByCode(String baseCurrencyCode) {
        final String query = "SELECT * FROM exchangeRates WHERE BaseCurrencyId =?";

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = SQLite.getConnection().prepareStatement(query);
            statement.setLong(1, currencyRepository.findByCode(baseCurrencyCode).get().getId());

            resultSet = statement.executeQuery();
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                exchangeRates.add(createExchangeRate(resultSet));
            }

            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            SQLite.closeConnection(statement, resultSet);
        }

    }

    public Optional<ExchangeRate> findByCodes(String baseCurrencyCode, String targetCurrencyCode) {
        final String query = "SELECT * FROM exchangeRates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = SQLite.getConnection().prepareStatement(query);
            statement.setLong(1, currencyRepository.findByCode(baseCurrencyCode).get().getId());
            statement.setLong(2, currencyRepository.findByCode(targetCurrencyCode).get().getId());

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.ofNullable(createExchangeRate(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            SQLite.closeConnection(statement, resultSet);
        }
        return Optional.empty();
    }

    @Override
    public List<ExchangeRate> findAll() {
        final String query = "SELECT * FROM exchangeRates";

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = SQLite.getConnection().prepareStatement(query);

            resultSet = statement.executeQuery();

            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                exchangeRates.add(createExchangeRate(resultSet));
            }

            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            SQLite.closeConnection(statement, resultSet);
        }

    }

    @Override
    public void save(ExchangeRate entity) {
        final String query = "INSERT INTO exchangeRates (BaseCurrencyId, TargetCurrencyId, rate) VALUES (?,?,?)";

        PreparedStatement statement = null;
        try {
            statement = SQLite.getConnection().prepareStatement(query);

            statement.setLong(1, entity.getBaseCurrency().getId());
            statement.setLong(2, entity.getTargetCurrency().getId());
            statement.setDouble(3, entity.getRate());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            SQLite.closeConnection(statement);
        }
    }

    @Override
    public void update(ExchangeRate entity) {
        final String query = "UPDATE exchangeRates SET BaseCurrencyId =?, TargetCurrencyId =?, rate =? WHERE id =?";

        PreparedStatement statement = null;
        try {
            statement = SQLite.getConnection().prepareStatement(query);

            statement.setLong(1, entity.getBaseCurrency().getId());
            statement.setLong(2, entity.getTargetCurrency().getId());
            statement.setDouble(3, entity.getRate());
            statement.setLong(4, entity.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            SQLite.closeConnection(statement);
        }

    }

    @Override
    public void delete(Long id) {
        final String query = "DELETE FROM exchangeRates WHERE id =?";

        try {
            PreparedStatement preparedStatement = SQLite.getConnection().prepareStatement(query);

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
