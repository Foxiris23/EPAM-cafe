package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.Command;
import com.jwd.cafe.command.CommandType;
import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.command.ResponseContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.*;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.OrderService;
import com.jwd.cafe.service.ProductService;
import com.jwd.cafe.service.UserService;
import com.jwd.cafe.util.JsonUtil;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.impl.DateTimeValidator;
import com.jwd.cafe.validator.impl.MethodValidator;
import com.jwd.cafe.validator.impl.StringValidator;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Log4j2
public class RestCheckoutCommand implements Command {
    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    public RestCheckoutCommand(UserService userService, ProductService productService, OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages = new DateTimeValidator(
                new StringValidator(
                        new MethodValidator(
                                new StringValidator(null, RequestConstant.CART)),
                        RequestConstant.ADDRESS)).validate(requestContext);

        if (violationMessages.isEmpty()) {
            try {
                String dateTimeStr = requestContext.getRequestParameters().get(RequestConstant.TIME);
                String address = requestContext.getRequestParameters().get(RequestConstant.ADDRESS);
                String method = requestContext.getRequestParameters().get(RequestConstant.METHOD);
                String json = requestContext.getRequestParameters().get(RequestConstant.CART);
                Map<Integer, Integer> cart = JsonUtil.jsonToCart(json);
                Map<Product, Integer> productsMap = productService.loadCart(cart);
                Optional<User> user = userService.findById((
                        (User) requestContext.getSessionAttributes().get(RequestConstant.USER)).getId());
                if (productsMap.size() > 0 && user.isPresent()) {
                    LocalDateTime deliveryDate = LocalDateTime.parse(dateTimeStr,
                            DateTimeFormatter.ofPattern(RequestConstant.TIME_PATTERN));
                    double totalCost = productService.calcTotalCost(productsMap);

                    Order order = Order.builder()
                            .withCost(totalCost).withAddress(address)
                            .withReviewCode(RandomStringUtils.randomAlphabetic(10))
                            .withCreateDate(LocalDateTime.now())
                            .withDeliveryDate(deliveryDate)
                            .withStatus(OrderStatus.ACTIVE).withMethod(PaymentMethod.valueOf(method))
                            .withUser(user.get())
                            .withProducts(productsMap).build();

                    Optional<String> serverMessage = orderService.create(order);
                    if (serverMessage.isEmpty()) {
                        return new ResponseContext(
                                Map.of(RequestConstant.REDIRECT_COMMAND, CommandType.USER_TO_ORDER_CONFIRM.getName()),
                                new HashMap<>());
                    }
                    return new ResponseContext(Map.of(RequestConstant.SERVER_MESSAGE, LocalizationHelper
                            .localize(requestContext.getLocale(), serverMessage.get())), new HashMap<>());
                }
            } catch (ServiceException e) {
                log.error("Failed to create order");
                return CommandType.ERROR.getCommand().execute(requestContext);
            }
        }
        return new ResponseContext(Map.of(RequestConstant.VIOLATION_MESSAGE, violationMessages),
                new HashMap<>());
    }
}
