package com.jwd.cafe.dao.impl;

import com.jwd.cafe.dao.AbstractDao;
import com.jwd.cafe.domain.ProductType;
import com.jwd.cafe.pool.DatabaseConnectionPool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ProductTypeDao extends AbstractDao<ProductType> {
    private static volatile ProductTypeDao instance;

    public static ProductTypeDao getInstance() {
        ProductTypeDao localInstance = instance;
        if (localInstance == null) {
            synchronized (ProductTypeDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ProductTypeDao(DatabaseConnectionPool.getInstance());
                }
            }
        }
        return localInstance;
    }

    private static final String SQL_FIND_ALL = "SELECT id, type_name, img_filename FROM product_type";
    private static final String SQL_CREATE = "INSERT INTO product_type (type_name, img_filename) VALUES (?, ?)";
    private static final String SQL_UPDATE = "UPDATE product_type SET type_name = ?, img_filename = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM product_type";
    private static final String SQL_COUNT = "SELECT COUNT(*) as count FROM product_type";


    @Override
    protected Optional<ProductType> parseResultSet(ResultSet resultSet) throws SQLException {
        return Optional.of(ProductType.builder()
                .withId(resultSet.getInt("id"))
                .withName(resultSet.getString("type_name"))
                .withFilename(resultSet.getString("img_filename"))
                .build()
        );
    }

    @Override
    protected String getFindAllSql() {
        return SQL_FIND_ALL;
    }

    @Override
    protected String getCreateSql() {
        return SQL_CREATE;
    }

    @Override
    protected String getUpdateSql() {
        return SQL_UPDATE;
    }

    @Override
    protected String getDeleteSql() {
        return SQL_DELETE;
    }

    @Override
    protected String getCountSql() {
        return SQL_COUNT;
    }

    @Override
    protected void prepareCreateStatement(PreparedStatement preparedStatement, ProductType entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getFilename());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement preparedStatement, ProductType entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getFilename());
        preparedStatement.setInt(3, entity.getId());
    }

    private ProductTypeDao(DatabaseConnectionPool pool) {
        super(pool);
    }
}
