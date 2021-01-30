package com.jwd.cafe.dao.impl;

import com.jwd.cafe.dao.specification.FindAll;
import com.jwd.cafe.dao.specification.FindUserById;
import com.jwd.cafe.domain.Role;
import com.jwd.cafe.domain.User;
import com.jwd.cafe.exception.DaoException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserDaoTest {
    private static TestDatabaseConf testDatabaseConf;

    @BeforeAll
    static void beforeAll() throws ClassNotFoundException {
        testDatabaseConf = new TestDatabaseConf();
        testDatabaseConf.initDatabase();
    }

    @Test
    public void successCRUDOperationsTest() throws DaoException {
        UserDao userDao = UserDao.getInstance();
        User user = User.builder().withUsername("username").withPassword("password").withEmail("email")
                .withIsBlocked(false).withBalance(0.0).withFirstName("firstName")
                .withLastName("lastName").withActivationCode("activationCode").withIsActive(true)
                .withRole(Role.USER).withLoyaltyPoints(0).withPhoneNumber("+375123457890").withId(2L).build();

        userDao.create(user);
        List<User> createUsers = userDao.findBySpecification(new FindUserById(2L));
        assertThat(createUsers).contains(user);

        user.setUsername("updateUsername");
        userDao.update(user);
        List<User> updateUsers = userDao.findBySpecification(new FindUserById(2L));
        assertThat(updateUsers).contains(user);

        userDao.deleteWithSpecification(new FindUserById(2L));
        List<User> deleteUsers = userDao.findBySpecification(new FindUserById(2L));
        assertThat(deleteUsers).doesNotContain(user);

        Long actualCount = userDao.countWithSpecification(new FindAll());
        assertThat(actualCount).isEqualTo(1L);
    }

    @AfterAll
    static void afterAll() throws SQLException {
        testDatabaseConf.destroyDatabase();
    }
}