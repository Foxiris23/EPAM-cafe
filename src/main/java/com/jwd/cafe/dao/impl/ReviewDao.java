package com.jwd.cafe.dao.impl;

import com.jwd.cafe.dao.AbstractDao;
import com.jwd.cafe.dao.specification.FindOrderById;
import com.jwd.cafe.domain.Order;
import com.jwd.cafe.domain.Review;
import com.jwd.cafe.exception.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ReviewDao extends AbstractDao<Review> {
    private static volatile ReviewDao instance;
    private final OrderDao orderDao;

    public static ReviewDao getInstance() {
        ReviewDao localInstance = instance;
        if (localInstance == null) {
            synchronized (ReviewDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ReviewDao();
                }
            }
        }
        return localInstance;
    }

    private final static String SQL_FIND_ALL = "SELECT id, feedback, rate, order_id FROM review";
    private static final String SQL_CREATE =
            "INSERT INTO review(feedback, rate, order_id) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE review set feedback = ?, rate = ?, order_id = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM review";
    private static final String SQL_COUNT = "SELECT COUNT(*) as count FROM review";

    @Override
    protected Optional<Review> parseResultSet(ResultSet resultSet) throws SQLException, DaoException {
        List<Order> orders =
                orderDao.findBySpecification(new FindOrderById(resultSet.getLong("order_id")));

        if (orders.size() > 0) {
            Review review = Review.builder()
                    .withId(resultSet.getLong("id"))
                    .withRate(resultSet.getInt("rate"))
                    .withFeedback(resultSet.getString("feedback"))
                    .withOrder(orders.get(0)).build();
            return Optional.of(review);
        }
        return Optional.empty();
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
    protected void prepareCreateStatement(PreparedStatement preparedStatement, Review entity) throws SQLException {
        prepareFullStatement(preparedStatement, entity);
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement preparedStatement, Review entity) throws SQLException {
        prepareFullStatement(preparedStatement, entity);
        preparedStatement.setLong(4, entity.getId());
    }

    private void prepareFullStatement(PreparedStatement preparedStatement, Review review) throws SQLException {
        preparedStatement.setString(1, review.getFeedback());
        preparedStatement.setInt(2, review.getRate());
        preparedStatement.setLong(3, review.getOrder().getId());
    }

    private ReviewDao() {
        orderDao = OrderDao.getInstance();
    }
}
