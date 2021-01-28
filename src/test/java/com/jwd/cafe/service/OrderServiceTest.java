package com.jwd.cafe.service;

import com.jwd.cafe.dao.impl.OrderDao;
import com.jwd.cafe.domain.Order;
import com.jwd.cafe.domain.PaymentMethod;
import com.jwd.cafe.domain.User;
import com.jwd.cafe.exception.DaoException;
import com.jwd.cafe.exception.ServiceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private OrderService orderService;
    @Mock
    private OrderDao orderDao;
    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        orderDao = mock(OrderDao.class);
        userService = mock(UserService.class);
        orderService = OrderService.getTestInstance(orderDao, userService);
    }


    @Test
    public void createShouldReturnEmptyMessage() throws DaoException, ServiceException {
        doNothing().when(orderDao).create(any(Order.class));
        doNothing().when(userService).updateUser(any(User.class));
        Order order = Order.builder()
                .withCost(1.0).withMethod(PaymentMethod.BALANCE)
                .withUser(User.builder().withBalance(1.0).withLoyaltyPoints(1).build())
                .build();

        assertThat(orderService.create(order).isEmpty()).isTrue();
    }

    @Test
    public void createShouldReturnInsufficientBalance() throws DaoException, ServiceException {
        doNothing().when(orderDao).create(any(Order.class));
        doNothing().when(userService).updateUser(any(User.class));
        Order order = Order.builder()
                .withCost(1.0).withMethod(PaymentMethod.BALANCE)
                .withUser(User.builder().withBalance(-10.0).withLoyaltyPoints(1).build())
                .build();

        assertThat(orderService.create(order).get()).isEqualTo("serverMessage.insufficientBalance");
    }

    @Test
    public void createShouldThrowServiceException() throws DaoException, ServiceException {
        doThrow(DaoException.class).when(orderDao).create(any(Order.class));
        doNothing().when(userService).updateUser(any(User.class));
        Order order = Order.builder()
                .withCost(1.0).withMethod(PaymentMethod.BALANCE)
                .withUser(User.builder().withBalance(10.0).withLoyaltyPoints(1).build())
                .build();

        assertThatThrownBy(() -> {
            orderService.create(order);
        }).isInstanceOf(ServiceException.class);
    }

    @AfterEach
    public void tearDown() {
        orderDao = null;
        userService = null;
        orderService = null;
    }
}