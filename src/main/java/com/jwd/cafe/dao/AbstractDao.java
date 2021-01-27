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

@Log4j2
public abstract class AbstractDao<T extends AbstractEntity> implements Dao<T> {
    protected final DatabaseConnectionPool databaseConnectionPool;

    protected AbstractDao() {
        databaseConnectionPool = DatabaseConnectionPool.getInstance();
    }

    protected abstract Optional<T> parseResultSet(ResultSet resultSet) throws SQLException, DaoException;

    protected abstract String getFindAllSql();

    protected abstract String getCreateSql();

    protected abstract String getUpdateSql();

    protected abstract String getDeleteSql();

    protected abstract String getCountSql();

    protected abstract void prepareCreateStatement(PreparedStatement preparedStatement, T entity) throws SQLException;

    protected abstract void prepareUpdateStatement(PreparedStatement preparedStatement, T entity) throws SQLException;

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
            throw new DaoException();
        }
        return entities;
    }

    @Override
    public void create(final T entity) throws DaoException {
        try (Connection connection = databaseConnectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(getCreateSql())) {
                prepareCreateStatement(preparedStatement, entity);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            log.error("Failed to create", e);
            throw new DaoException();
        }
    }

    @Override
    public T update(final T entity) throws DaoException {
        try (Connection connection = databaseConnectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(getUpdateSql())) {
                prepareUpdateStatement(preparedStatement, entity);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            log.error("Failed to update", e);
            throw new DaoException();
        }
        return entity;
    }

    @Override
    public void deleteWithSpecification(final Specification specification) throws DaoException {
        System.out.println(getDeleteSql() + specification.specify());
        try (Connection connection = databaseConnectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    getDeleteSql() + specification.specify())) {
                specification.prepareSpecification(preparedStatement);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            log.error("Failed to delete with specification", e);
            throw new DaoException();
        }
    }

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
            throw new DaoException();
        }
        return 0L;
    }
}
