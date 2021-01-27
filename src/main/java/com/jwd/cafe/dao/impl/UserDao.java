package com.jwd.cafe.dao.impl;

import com.jwd.cafe.dao.AbstractDao;
import com.jwd.cafe.domain.Role;
import com.jwd.cafe.domain.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDao extends AbstractDao<User> {
    private static volatile UserDao instance;

    public static UserDao getInstance() {
        UserDao localInstance = instance;
        if (localInstance == null) {
            synchronized (UserDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UserDao();
                }
            }
        }
        return localInstance;
    }

    private static final String SQL_FIND_ALL =
            "SELECT bu.id, username, password, first_name, last_name, is_blocked, role_id, email, activation_code," +
                    " phone_number, is_active, balance, loyalty_points, role_name, ur.id FROM base_user as bu" +
                    " INNER JOIN user_role AS ur ON ur.id = bu.id";
    private static final String SQL_CREATE =
            "INSERT INTO base_user(username, password, first_name, last_name, is_blocked, role_id, email," +
                    " activation_code, phone_number, is_active, balance, loyalty_points)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE =
            "UPDATE base_user SET username = ?, password = ?, first_name = ?, last_name = ?, is_blocked = ?, role_id = ?," +
                    " email = ?, activation_code = ?, phone_number = ?, is_active = ?, balance = ?, loyalty_points = ?" +
                    " WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM base_user WHERE id = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) as count FROM base_user";


    @Override
    protected Optional<User> parseResultSet(ResultSet resultSet) throws SQLException {
        User baseUser;
        Role role = new Role(resultSet.getInt("ur.id"), resultSet.getString("role_name"));
        baseUser = User.builder()
                .withId(resultSet.getLong("id"))
                .withUsername(resultSet.getString("username"))
                .withPassword(resultSet.getString("password"))
                .withFirstName(resultSet.getString("first_name"))
                .withLastName(resultSet.getString("last_name"))
                .withIsBlocked(resultSet.getBoolean("is_blocked"))
                .withRole(role)
                .withActivationCode(resultSet.getString("activation_code"))
                .withBalance(resultSet.getDouble("balance"))
                .withEmail(resultSet.getString("email"))
                .withIsActive(resultSet.getBoolean("is_active"))
                .withLoyaltyPoints(resultSet.getInt("loyalty_points"))
                .withPhoneNumber(resultSet.getString("phone_number"))
                .build();
        return Optional.of(baseUser);
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
    protected void prepareCreateStatement(PreparedStatement preparedStatement, User entity) throws SQLException {
        prepareFullUserStatement(entity, preparedStatement);
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement preparedStatement, User entity) throws SQLException {
        prepareFullUserStatement(entity, preparedStatement);
        preparedStatement.setLong(13, entity.getId());
    }

    private void prepareFullUserStatement(User entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, entity.getUsername());
        preparedStatement.setString(2, entity.getPassword());
        preparedStatement.setString(3, entity.getFirstName());
        preparedStatement.setString(4, entity.getLastName());
        preparedStatement.setBoolean(5, entity.getIsBlocked());
        preparedStatement.setInt(6, entity.getRole().getId());
        preparedStatement.setString(7, entity.getEmail());
        preparedStatement.setString(8, entity.getActivationCode());
        preparedStatement.setString(9, entity.getPhoneNumber());
        preparedStatement.setBoolean(10, entity.getIsActive());
        preparedStatement.setDouble(11, entity.getBalance());
        preparedStatement.setInt(12, entity.getLoyaltyPoints());
    }

    private UserDao() {
    }
}
