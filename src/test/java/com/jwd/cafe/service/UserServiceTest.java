package com.jwd.cafe.service;

import com.jwd.cafe.dao.impl.UserDao;
import com.jwd.cafe.dao.specification.FindByActivationCode;
import com.jwd.cafe.dao.specification.FindByEmail;
import com.jwd.cafe.dao.specification.FindByUsername;
import com.jwd.cafe.dao.specification.Specification;
import com.jwd.cafe.domain.User;
import com.jwd.cafe.exception.DaoException;
import com.jwd.cafe.exception.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserDao userDao;
    private UserService userService;

    @Before
    public void beforeTests() throws DaoException {
        userDao = mock(UserDao.class);
        userService = UserService.getTestInstance(userDao);
        doNothing().when(userDao).create(any(User.class));
    }

    @Test
    public void loginShouldReturnEmptyMessage() throws DaoException, ServiceException {
        when(userDao.findBySpecification(any(FindByUsername.class))).thenReturn(List.of(User.builder()
                .withPassword("$s0$41010$tVlOEZSsRuI/eAdqIR/QFA==$UhnfgeoQVjDdL0J6do3Gt6gI4x1Os9XTvj814zQOyrg=")
                .withUsername("test").withIsBlocked(false).withIsActive(true).build()));
        assertThat(userService.login("test", "Mm12345", new HashMap<>()).isEmpty()).isTrue();
    }

    @Test
    public void loginShouldReturnIncorrectLoginAndPasswordWhenUserIsNotPresent() throws DaoException, ServiceException {
        when(userDao.findBySpecification(any(FindByUsername.class))).thenReturn(new ArrayList<>());

        assertThat(userService.login("test", "test", new HashMap<>()).get())
                .isEqualTo("serverMessage.incorrectUsernameOrPassword");
    }

    @Test
    public void loginShouldReturnIncorrectLoginAndPasswordWhenPasswordIsIncorrect() throws DaoException, ServiceException {
        when(userDao.findBySpecification(any(Specification.class))).thenReturn(List.of(User.builder()
                .withPassword("$s0$41010$tVlOEZSsRuI/eAdqIR/QFA==$UhnfgeoQVjDdL0J6do3Gt6gI4x1Os9XTvj814zQOyrg=")
                .withUsername("test").withIsBlocked(false).withIsActive(true).build()));

        assertThat(userService.login("test", "test", new HashMap<>()).get())
                .isEqualTo("serverMessage.incorrectUsernameOrPassword");
    }

    @Test
    public void loginShouldReturnActivatePlease() throws DaoException, ServiceException {
        when(userDao.findBySpecification(any(FindByUsername.class))).thenReturn(List.of(User.builder()
                .withPassword("$s0$41010$tVlOEZSsRuI/eAdqIR/QFA==$UhnfgeoQVjDdL0J6do3Gt6gI4x1Os9XTvj814zQOyrg=")
                .withUsername("test").withIsBlocked(false).withIsActive(false).build()));

        assertThat(userService.login("test", "Mm12345", new HashMap<>()).get())
                .isEqualTo("serverMessage.activateAccountPlease");
    }

    @Test
    public void loginShouldReturnBlockedAccount() throws DaoException, ServiceException {
        when(userDao.findBySpecification(any(FindByUsername.class))).thenReturn(List.of(User.builder()
                .withPassword("$s0$41010$tVlOEZSsRuI/eAdqIR/QFA==$UhnfgeoQVjDdL0J6do3Gt6gI4x1Os9XTvj814zQOyrg=")
                .withUsername("test").withIsBlocked(true).withIsActive(true).build()));

        assertThat(userService.login("test", "Mm12345", new HashMap<>()).get())
                .isEqualTo("serverMessage.blockedAccount");
    }

    @Test
    public void registerShouldReturnEmptyMessage() throws DaoException, ServiceException {
        User user = User.builder().withUsername("test").withEmail("test@mail.com").build();
        when(userDao.findBySpecification(any(Specification.class))).thenReturn(new ArrayList<>());


        assertThat(userService.register(user)
                .isEmpty()).isTrue();
    }

    @Test
    public void registerShouldReturnUsernameAlreadyTaken() throws DaoException, ServiceException {
        User user = User.builder().withUsername("test").withEmail("test@mail.com").build();
        when(userDao.findBySpecification(any(FindByUsername.class)))
                .thenReturn(List.of(User.builder().withUsername("test").build()));

        assertThat(userService.register(user)
                .get()).isEqualTo("serverMessage.usernameAlreadyTaken");
    }

    @Test
    public void registerShouldReturnEmailAlreadyTaken() throws DaoException, ServiceException {
        User user = User.builder().withUsername("test").withEmail("test@mail.com").build();
        when(userDao.findBySpecification(any(FindByUsername.class)))
                .thenReturn(new ArrayList<>());
        when(userDao.findBySpecification(any(FindByEmail.class)))
                .thenReturn(List.of(User.builder().withEmail("test@mail.com").build()));

        assertThat(userService.register(user)
                .get()).isEqualTo("serverMessage.emailAlreadyTaken");
    }

    @Test
    public void activateUserShouldReturnEmptyMessage() throws DaoException, ServiceException {
        when(userDao.findBySpecification(any(FindByActivationCode.class)))
                .thenReturn(List.of(User.builder().build()));
        when(userDao.update(any(User.class))).thenReturn(any(User.class));

        assertThat(userService.activateUser(UUID.randomUUID().toString()).isEmpty()).isTrue();
    }

    @Test
    public void activateUserShouldReturnIncorrectActivationCode() throws DaoException, ServiceException {
        when(userDao.findBySpecification(any(FindByActivationCode.class)))
                .thenReturn(new ArrayList<>());
        when(userDao.update(any(User.class))).thenReturn(any(User.class));

        assertThat(userService.activateUser(UUID.randomUUID().toString()).get())
                .isEqualTo("serverMessage.incorrectActivationCode");
    }
}