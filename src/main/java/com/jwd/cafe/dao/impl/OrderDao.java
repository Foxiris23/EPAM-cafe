package com.jwd.cafe.dao.impl;

import com.jwd.cafe.dao.AbstractDao;
import com.jwd.cafe.dao.specification.FindUserById;
import com.jwd.cafe.domain.*;
import com.jwd.cafe.exception.DaoException;
import com.jwd.cafe.exception.EntityNotFoundException;
import com.jwd.cafe.pool.DatabaseConnectionPool;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
public class OrderDao extends AbstractDao<Order> {
    private static volatile OrderDao instance;
    private final UserDao userDao;
    private final ProductDao productDao;

    public static OrderDao getInstance() {
        OrderDao localInstance = instance;
        if (localInstance == null) {
            synchronized (OrderDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new OrderDao(
                            DatabaseConnectionPool.getInstance(), UserDao.getInstance(), ProductDao.getInstance());
                }
            }
        }
        return localInstance;
    }

    private static final String SQL_FIND_ALL =
            "SELECT id, address, review_code, cost, create_date, delivery_date," +
                    " status_id, method_id, user_id FROM cafe_order";
    private static final String SQL_CREATE =
            "INSERT INTO cafe_order(address, review_code, cost, create_date," +
                    " delivery_date, status_id, method_id, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE cafe_order SET address = ?, review_code = ?, cost = ?, create_date = ?, delivery_date = ?," +
                    " status_id = ?, method_id = ?, user_id = ? WHERE id= ?";
    private static final String SQL_DELETE = "DELETE FROM cafe_order";
    private static final String SQL_COUNT = "SELECT COUNT(*) as count FROM cafe_order";
    private static final String SQL_CREATE_ORDERS_PRODUCTS =
            "INSERT INTO order_product(order_id, product_id, amount) VALUES (?, ?, ?)";


    @Override
    protected Optional<Order> parseResultSet(ResultSet resultSet) throws SQLException, DaoException {
        List<User> users =
                userDao.findBySpecification(new FindUserById(resultSet.getLong("user_id")));
        if (users.size() < 1) {
            log.error("Failed to load users from product dao");
            throw new DaoException();
        }
        try {
            long id = resultSet.getLong("id");
            Order order = Order.builder()
                    .withId(id)
                    .withAddress(resultSet.getString("address"))
                    .withReviewCode(resultSet.getString("review_code"))
                    .withCost(resultSet.getDouble("cost"))
                    .withCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime())
                    .withDeliveryDate(resultSet.getTimestamp("delivery_date").toLocalDateTime())
                    .withStatus(OrderStatus.resolveStatusById(resultSet.getInt("status_id")))
                    .withMethod(PaymentMethod.resolveMethodById(resultSet.getInt("method_id")))
                    .withUser(users.get(0))
                    .withProducts(productDao.findOrdersProductsByOrderId(id))
                    .build();
            return Optional.of(order);
        } catch (EntityNotFoundException e) {
            log.error("Failed to parse order result set");
            return Optional.empty();
        }
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
    protected void prepareCreateStatement(PreparedStatement preparedStatement, Order entity) throws SQLException {
        prepareFullOrderStatement(preparedStatement, entity);
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement preparedStatement, Order entity) throws SQLException {
        prepareFullOrderStatement(preparedStatement, entity);
        preparedStatement.setLong(9, entity.getId());
    }

    private void prepareFullOrderStatement(PreparedStatement preparedStatement, Order order) throws SQLException {
        preparedStatement.setString(1, order.getAddress());
        preparedStatement.setString(2, order.getReviewCode());
        preparedStatement.setDouble(3, order.getCost());
        preparedStatement.setTimestamp(4, Timestamp.valueOf(order.getCreateDate()));
        preparedStatement.setTimestamp(5, Timestamp.valueOf(order.getDeliveryDate()));
        preparedStatement.setInt(6, order.getStatus().getId());
        preparedStatement.setInt(7, order.getMethod().getId());
        preparedStatement.setLong(8, order.getUser().getId());
    }

    @Override
    public void create(Order entity) throws DaoException {
        try (Connection connection = databaseConnectionPool.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatementCreateOrder = null;
            PreparedStatement preparedStatementProduct = null;
            try {
                preparedStatementCreateOrder = connection.prepareStatement(SQL_CREATE);
                prepareFullOrderStatement(preparedStatementCreateOrder, entity);
                preparedStatementCreateOrder.execute();
                preparedStatementCreateOrder.close();
                ResultSet resultSet = connection.prepareStatement("select LAST_INSERT_ID() as id").executeQuery();
                if (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    for (Map.Entry<Product, Integer> entry : entity.getProducts().entrySet()) {
                        preparedStatementProduct = connection.prepareStatement(
                                "INSERT INTO order_product(order_id, product_id, amount) VALUES (?, ?, ?)");
                        preparedStatementProduct.setLong(1, id);
                        preparedStatementProduct.setInt(2, entry.getKey().getId());
                        preparedStatementProduct.setInt(3, entry.getValue());
                        preparedStatementProduct.execute();
                        preparedStatementProduct.close();
                    }
                    connection.commit();
                } else {
                    throw new SQLException();
                }
            } catch (SQLException e) {
                log.error("Failed to create order");
                if (preparedStatementCreateOrder != null) {
                    preparedStatementCreateOrder.close();
                }
                connection.rollback();
            } finally {
                if (preparedStatementCreateOrder != null) {
                    preparedStatementCreateOrder.close();
                }
                if (preparedStatementProduct != null) {
                    preparedStatementProduct.close();
                }
            }
        } catch (SQLException e) {
            log.error("Failed to create order");
            throw new DaoException();
        }
    }

    private OrderDao(DatabaseConnectionPool databaseConnectionPool, UserDao userDao, ProductDao productDao) {
        super(databaseConnectionPool);
        this.userDao = userDao;
        this.productDao = productDao;
    }
}
