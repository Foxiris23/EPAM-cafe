package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.PageConstant;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.Order;
import com.jwd.cafe.domain.User;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.OrderService;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class ToMyOrdersCommand implements Command {
    private final OrderService orderService;

    public ToMyOrdersCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        try {
            List<Order> orders = orderService
                    .findAllUsersOrders((User) requestContext.getSessionAttributes().get(RequestConstant.USER));
            return new ResponseContext(new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.MY_ORDERS),
                    Map.of(RequestConstant.ORDERS, orders), new HashMap<>());
        } catch (ServiceException e) {
            log.error("Failed to find users products");
        }
        return CommandType.ERROR.getCommand().execute(requestContext);
    }
}
