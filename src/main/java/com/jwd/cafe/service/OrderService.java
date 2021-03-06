package com.jwd.cafe.service;

import com.jwd.cafe.config.AppConfig;
import com.jwd.cafe.dao.impl.OrderDao;
import com.jwd.cafe.dao.specification.*;
import com.jwd.cafe.domain.Order;
import com.jwd.cafe.domain.OrderStatus;
import com.jwd.cafe.domain.PaymentMethod;
import com.jwd.cafe.domain.User;
import com.jwd.cafe.exception.DaoException;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.mail.OrderDetailsMailSender;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;

/**
 * The class provides a business logic of application connected with {@link Order}
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class OrderService {
    private static volatile OrderService instance;
    private final OrderDao orderDao;
    private final UserService userService;

    public static OrderService getInstance() {
        OrderService localInstance = instance;
        if (localInstance == null) {
            synchronized (OrderService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new OrderService(OrderDao.getInstance(), UserService.getInstance());
                }
            }
        }
        return localInstance;
    }

    public Optional<String> create(Order order) throws ServiceException {
        try {
            User user = order.getUser();
            if (user.getIsBlocked()) {
                return Optional.of("serverMessage.blockedAccount");
            }
            if (user.getBalance() < 0 && order.getMethod().equals(PaymentMethod.BALANCE)) {
                return Optional.of("serverMessage.insufficientBalance");
            }
            orderDao.create(order);
            double cost = order.getCost();
            if (order.getMethod().equals(PaymentMethod.BALANCE)) {
                user.setBalance(user.getBalance() - cost);
            }
            user.setLoyaltyPoints(user.getLoyaltyPoints() + AppConfig.getInstance().getPointsPerDollar() * (int) cost);
            userService.updateUser(user);
            Runnable emailSender = new OrderDetailsMailSender(user.getEmail(), order.getReviewCode());
            Executors.newSingleThreadExecutor().submit(emailSender);
        } catch (DaoException e) {
            log.error("Failed to create order, dao provided exception");
            throw new ServiceException(e);
        }
        return Optional.empty();
    }

    public List<Order> findAllOrdersByUserId(Long id) throws ServiceException {
        try {
            return orderDao.findBySpecification(new FindOrderByUserId(id));
        } catch (DaoException e) {
            log.error("Failed to find users orders, dao provided exception");
            throw new ServiceException(e);
        }
    }

    public Optional<Order> findOrderByUserIdAndReviewCode(Long id, String code) throws ServiceException {
        try {
            List<Order> orders = orderDao.findBySpecification(
                    new FindOrderByUserIdAndCode(id, code));
            if (orders.size() > 0) {
                return Optional.of(orders.get(0));
            }
        } catch (DaoException e) {
            log.error("Failed to find order by user and review code");
            throw new ServiceException(e);
        }
        return Optional.empty();
    }

    public Long countOrders() throws ServiceException {
        try {
            return orderDao.countWithSpecification(new CountAll());
        } catch (DaoException e) {
            log.error("Failed to count orders");
            throw new ServiceException(e);
        }
    }

    public List<Order> findAll(Integer page, Integer perPage) throws ServiceException {
        try {
            Long offset = ((long) (perPage) * (page - 1));
            return orderDao.findBySpecification(new FindAllOrdersSortByStatus(perPage, offset));
        } catch (DaoException e) {
            log.error("Failed to load all orders");
            throw new ServiceException(e);
        }
    }

    public Optional<Order> findById(Long orderId) throws ServiceException {
        try {
            List<Order> orders = orderDao.findBySpecification(new FindOrderById(orderId));
            if (orders.size() > 0) {
                return Optional.of(orders.get(0));
            }
        } catch (DaoException e) {
            log.error("Failed to find order by id");
            throw new ServiceException(e);
        }
        return Optional.empty();
    }

    public void updateOrder(Order order) throws ServiceException {
        try {
            orderDao.update(order);
            User user = order.getUser();
            double cost = order.getCost();
            AppConfig appConfig = AppConfig.getInstance();
            Integer pointsPerDollar = appConfig.getPointsPerDollar();
            PaymentMethod paymentMethod = order.getMethod();
            OrderStatus orderStatus = order.getStatus();
            if (orderStatus.equals(OrderStatus.CANCELLED)) {
                if (paymentMethod.equals(PaymentMethod.BALANCE)) {
                    user.setBalance(user.getBalance() + cost);
                }
                user.setLoyaltyPoints(user.getLoyaltyPoints() - (int) cost * pointsPerDollar);
            }
            if (orderStatus.equals(OrderStatus.UNACCEPTED) && !paymentMethod.equals(PaymentMethod.BALANCE)) {
                user.setLoyaltyPoints(
                        user.getLoyaltyPoints() - (int) cost * pointsPerDollar - pointsPerDollar);
                if (user.getLoyaltyPoints() < appConfig.getMinusToBlock()) {
                    user.setIsBlocked(true);
                }
            }
            userService.updateUser(user);
        } catch (DaoException e) {
            log.error("Failed to update order");
            throw new ServiceException(e);
        }
    }

    private OrderService(OrderDao orderDao, UserService userService) {
        this.orderDao = orderDao;
        this.userService = userService;
    }

    @VisibleForTesting
    public static OrderService getTestInstance(OrderDao orderDao, UserService userService) {
        return new OrderService(orderDao, userService);
    }
}
