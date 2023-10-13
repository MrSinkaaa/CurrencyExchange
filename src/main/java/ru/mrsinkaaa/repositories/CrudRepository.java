package ru.mrsinkaaa.repositories;

import ru.mrsinkaaa.dto.CurrencyDTO;
import ru.mrsinkaaa.entity.Currency;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {

    Optional<T> findById(Long id);

    List<T> findAll() throws ClassNotFoundException;

    void save(T entity);

    void update(T entity);

    void delete(Long id);
}
