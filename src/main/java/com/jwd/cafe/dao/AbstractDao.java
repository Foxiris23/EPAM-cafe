package com.jwd.cafe.dao;

import com.jwd.cafe.dao.specification.Specification;
import com.jwd.cafe.domain.AbstractEntity;
import com.jwd.cafe.exception.DaoException;
import com.jwd.cafe.pool.DatabaseConnectionPool;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Provides base realisation of CRUD operations for working with the entities
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public abstract class AbstractDao<T extends AbstractEntity> implements Dao<T> {
    protected final DatabaseConnectionPool databaseConnectionPool;

    protected AbstractDao(DatabaseConnectionPool databaseConnectionPool) {
        this.databaseConnectionPool = databaseConnectionPool;
    }

    /**
     * @param resultSet contains all data received form database
     * @return {@link Optional} of {@link AbstractEntity}
     * @throws DaoException if the parsing failed
     */
    protected abstract Optional<T> parseResultSet(ResultSet resultSet) throws SQLException, DaoException;

    /**
     * @return {@link String} of select all sql query
     */
    protected abstract String getFindAllSql();

    /**
     * @return {@link String} of create sql query
     */
    protected abstract String getCreateSql();

    /**
     * @return {@link String} of update sql query
     */
    protected abstract String getUpdateSql();

    /**
     * @return {@link String} of delete sql query
     */
    protected abstract String getDeleteSql();

    /**
     * @return {@link String} of count all sql query
     */
    protected abstract String getCountSql();

    /**
     * @param preparedStatement contains create sql query to be prepared
     * @throws SQLException if the setting statement parameters failed
     */
    protected abstract void prepareCreateStatement(PreparedStatement preparedStatement, T entity) throws SQLException;

    /**
     * @param preparedStatement contains update sql query to be prepared
     * @throws SQLException if the setting statement parameters failed
     */
    protected abstract void prepareUpdateStatement(PreparedStatement preparedStatement, T entity) throws SQLException;

    /**
     * @param specification contains an addition to sql query
     * @throws DaoException if executing of {@link PreparedStatement} or parsing of {@link ResultSet} fails
     */
    @Override
    public List<T> findBySpecification(final Specification specification) throws DaoException {
        List<T> entities = new ArrayList<>();
        try (Connection connection = databaseConnectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    getFindAllSql() + specification.specify())
            ) {
                specification.prepareSpecification(preparedStatement);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Optional<T> baseUserOptional = parseResultSet(resultSet);
                    baseUserOptional.ifPresent(entities::add);
                }
            }
        } catch (SQLException e) {
            log.error("Failed to find by specification:", e);
            throw new DaoException(e);
        }
        return entities;
    }

    /**
     * @param entity entity to create
     * @throws DaoException if executing of {@link PreparedStatement}
     */
    @Override
    public void create(final T entity) throws DaoException {
        try (Connection connection = databaseConnectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(getCreateSql())) {
                prepareCreateStatement(preparedStatement, entity);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            log.error("Failed to create", e);
            throw new DaoException(e);
        }
    }

    /**
     * @param entity entity to update
     * @throws DaoException if executing of {@link PreparedStatement}
     */
    @Override
    public T update(final T entity) throws DaoException {
        try (Connection connection = databaseConnectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(getUpdateSql())) {
                prepareUpdateStatement(preparedStatement, entity);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            log.error("Failed to update", e);
            throw new DaoException(e);
        }
        return entity;
    }

    /**
     * @param specification contains an addition to sql query
     * @throws DaoException if executing of {@link PreparedStatement}
     */
    @Override
    public void deleteWithSpecification(final Specification specification) throws DaoException {
        try (Connection connection = databaseConnectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    getDeleteSql() + specification.specify())) {
                specification.prepareSpecification(preparedStatement);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            log.error("Failed to delete with specification", e);
            throw new DaoException(e);
        }
    }

    /**
     * @param specification contains an addition to sql query
     * @throws DaoException if executing of {@link PreparedStatement}
     */
    @Override
    public Long countWithSpecification(final Specification specification) throws DaoException {
        try (Connection connection = databaseConnectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    getCountSql() + specification.specify())
            ) {
                specification.prepareSpecification(preparedStatement);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getLong("count");
                }
            }
        } catch (SQLException e) {
            log.error("Failed to count with specification", e);
            throw new DaoException(e);
        }
        return 0L;
    }
}
