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

    private final SQLite sqlite = new SQLite();
    private final CurrencyRepository currencyRepository = new CurrencyRepository();

    public ExchangeRatesRepository() {
    }

    @Override
    public Optional<ExchangeRate> findById(Long id) {
        final String query = "SELECT * FROM exchangeRates WHERE id = ?";

        try {
            PreparedStatement preparedStatement = sqlite.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.ofNullable(createExchangeRate(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public List<ExchangeRate> findByCode(String baseCurrencyCode) {
        final String query = "SELECT * FROM exchangeRates WHERE BaseCurrencyId =?";

        try {
            PreparedStatement preparedStatement = sqlite.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, currencyRepository.findByCode(baseCurrencyCode).get().getId());

            ResultSet resultSet = preparedStatement.executeQuery();
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                exchangeRates.add(createExchangeRate(resultSet));
            }

            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Optional<ExchangeRate> findByCodes(String baseCurrencyCode, String targetCurrencyCode) {
        final String query = "SELECT * FROM exchangeRates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";

        try {
            PreparedStatement preparedStatement = sqlite.getConnection().prepareStatement(query);
            preparedStatement.setLong(1, currencyRepository.findByCode(baseCurrencyCode).get().getId());
            preparedStatement.setLong(2, currencyRepository.findByCode(targetCurrencyCode).get().getId());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.ofNullable(createExchangeRate(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<ExchangeRate> findAll() {
        final String query = "SELECT * FROM exchangeRates";

        try {
            PreparedStatement preparedStatement = sqlite.getConnection().prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                exchangeRates.add(createExchangeRate(resultSet));
            }

            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void save(ExchangeRate entity) {
        final String query = "INSERT INTO exchangeRates (BaseCurrencyId, TargetCurrencyId, rate) VALUES (?,?,?)";

        try {
            PreparedStatement preparedStatement = sqlite.getConnection().prepareStatement(query);

            preparedStatement.setLong(1, entity.getBaseCurrency().getId());
            preparedStatement.setLong(2, entity.getTargetCurrency().getId());
            preparedStatement.setDouble(3, entity.getRate());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(ExchangeRate entity) {
        final String query = "UPDATE exchangeRates SET BaseCurrencyId =?, TargetCurrencyId =?, rate =? WHERE id =?";

        try {
            PreparedStatement preparedStatement = sqlite.getConnection().prepareStatement(query);

            preparedStatement.setLong(1, entity.getBaseCurrency().getId());
            preparedStatement.setLong(2, entity.getTargetCurrency().getId());
            preparedStatement.setDouble(3, entity.getRate());
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(Long id) {
        final String query = "DELETE FROM exchangeRates WHERE id =?";

        try {
            PreparedStatement preparedStatement = sqlite.getConnection().prepareStatement(query);

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
