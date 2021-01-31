package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.Command;
import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.command.ResponseContext;
import com.jwd.cafe.command.RestCommandType;
import com.jwd.cafe.config.AppConfig;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.Order;
import com.jwd.cafe.domain.OrderStatus;
import com.jwd.cafe.domain.User;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.OrderService;
import com.jwd.cafe.service.UserService;
import com.jwd.cafe.validator.impl.IntValidator;
import com.jwd.cafe.validator.impl.StringValidator;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Allows admin to update an {@link com.jwd.cafe.domain.Order}
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class RestUpdateOrderCommand implements Command {
    private final UserService userService;
    private final OrderService orderService;

    public RestUpdateOrderCommand(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     * executes {@link RestErrorCommand} if {@link ServiceException} was caught
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationsMessages = new IntValidator(new StringValidator(null, RequestConstant.SELECT),
                RequestConstant.ID).validate(requestContext);

        if (violationsMessages.isEmpty()) {
            try {
                Long orderId = Long.parseLong(requestContext.getRequestParameters().get(RequestConstant.ID));
                OrderStatus orderStatus =
                        OrderStatus.valueOf(requestContext.getRequestParameters().get(RequestConstant.SELECT));
                Optional<Order> orderOptional = orderService.findById(orderId);
                if (orderOptional.isEmpty()) {
                    return RestCommandType.ERROR.getCommand().execute(requestContext);
                }
                Order order = orderOptional.get();
                if (orderStatus.equals(OrderStatus.UNACCEPTED)) {
                    User user = order.getUser();
                    double cost = order.getCost();
                    AppConfig appConfig = AppConfig.getInstance();
                    user.setLoyaltyPoints(
                            user.getLoyaltyPoints() - (int) cost * appConfig.getPointsPerDollar());
                    if (user.getLoyaltyPoints() < appConfig.getMinusToBlock()) {
                        user.setIsBlocked(true);
                    }
                    userService.updateUser(user);
                }
                order.setStatus(orderStatus);
                orderService.updateOrder(order);
                return new ResponseContext(new HashMap<>(), new HashMap<>());
            } catch (ServiceException e) {
                log.error("Failed to update order");
            }
        }
        return new ResponseContext(Map.of(RequestConstant.VIOLATION_MESSAGE, violationsMessages), new HashMap<>());
    }
}
