package com.jwd.cafe.dao;

import com.jwd.cafe.dao.specification.Specification;
import com.jwd.cafe.exception.DaoException;

import java.util.List;

/**
 * The interface provides CRUD operations
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public interface Dao<T> {
    List<T> findBySpecification(Specification specification) throws DaoException;

    void create(T entity) throws DaoException;

    T update(T entity) throws DaoException;

    void deleteWithSpecification(final Specification specification) throws DaoException;

    Long countWithSpecification(Specification specification) throws DaoException;
}