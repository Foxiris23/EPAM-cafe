package com.jwd.cafe.dao.impl;

import com.jwd.cafe.dao.AbstractDao;
import com.jwd.cafe.dao.specification.FindProductTypeById;
import com.jwd.cafe.domain.Order;
import com.jwd.cafe.domain.Product;
import com.jwd.cafe.domain.ProductType;
import com.jwd.cafe.exception.DaoException;
import com.jwd.cafe.pool.DatabaseConnectionPool;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Provides CRUD operations for working with the {@link Product}
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class ProductDao extends AbstractDao<Product> {
    private static volatile ProductDao instance;
    private final ProductTypeDao productTypeDao;

    public static ProductDao getInstance() {
        ProductDao localInstance = instance;
        if (localInstance == null) {
            synchronized (ProductDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ProductDao(
                            DatabaseConnectionPool.getInstance(), ProductTypeDao.getInstance());
                }
            }
        }
        return localInstance;
    }

    private static final String SQL_FIND_ALL =
            "SELECT id, product_name, price, img_filename, description, type_id FROM product";
    private static final String SQL_CREATE =
            "INSERT INTO product(product_name, price, img_filename, description, type_id)" +
                    " VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE product SET product_name = ?, price = ?, img_filename = ?," +
                    " description = ?, type_id = ? WHERE id= ?";
    private static final String SQL_DELETE = "DELETE FROM product";
    private static final String SQL_COUNT = "SELECT COUNT(*) as count FROM product";
    private static final String SQL_FIND_BY_ORDER_ID =
            "SELECT id, product_name, price, img_filename, description, type_id, amount FROM product" +
                    " INNER JOIN order_product as op on product.id = op.product_id WHERE op.order_id = ?";

    @Override
    protected Optional<Product> parseResultSet(ResultSet resultSet) throws SQLException, DaoException {
        Product product;
        List<ProductType> productTypeList = productTypeDao.findBySpecification(
                new FindProductTypeById(resultSet.getInt("type_id")));
        if (productTypeList.size() < 1) {
            log.warn("Failed to load product type in product");
            throw new DaoException();
        }
        product = Product.builder()
                .withId(resultSet.getInt("id"))
                .withName(resultSet.getString("product_name"))
                .withPrice(resultSet.getDouble("price"))
                .withImgFilename(resultSet.getString("img_filename"))
                .withDescription(resultSet.getString("description"))
                .withProductType(productTypeList.get(0))
                .build();

        return Optional.of(product);
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
    protected void prepareCreateStatement(PreparedStatement preparedStatement, Product entity) throws SQLException {
        prepareFullProductStatement(preparedStatement, entity);
    }

    private void prepareFullProductStatement(PreparedStatement preparedStatement, Product entity) throws SQLException {
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setDouble(2, entity.getPrice());
        preparedStatement.setString(3, entity.getImgFilename());
        preparedStatement.setString(4, entity.getDescription());
        preparedStatement.setInt(5, entity.getProductType().getId());
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement preparedStatement, Product entity) throws SQLException {
        prepareFullProductStatement(preparedStatement, entity);
        preparedStatement.setInt(6, entity.getId());
    }

    public Map<Product, Integer> findOrdersProductsByOrderId(Long id) throws DaoException {
        Map<Product, Integer> products = new HashMap<>();
        try (Connection connection = databaseConnectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ORDER_ID)
            ) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Optional<Product> baseUserOptional = parseResultSet(resultSet);
                    Integer amount = resultSet.getInt("amount");
                    baseUserOptional.ifPresent(product -> products.put(product, amount));
                }
            }
        } catch (SQLException e) {
            //todo log
            throw new DaoException();
        }
        return products;
    }

    private ProductDao(DatabaseConnectionPool pool, ProductTypeDao productTypeDao) {
        super(pool);
        this.productTypeDao = productTypeDao;
    }
}
