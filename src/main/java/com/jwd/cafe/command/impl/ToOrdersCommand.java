package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.PageConstant;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.Order;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.OrderService;
import com.jwd.cafe.util.Pageable;
import com.jwd.cafe.validator.impl.IntValidator;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Moves an admin to orders page
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class ToOrdersCommand implements Command{
    private static final Integer PER_PAGE = 30;
    private final OrderService orderService;

    public ToOrdersCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     * executes {@link ErrorCommand} if {@link ServiceException} was caught
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages = new IntValidator(null, RequestConstant.PAGE).validate(requestContext);

        if (violationMessages.isEmpty()) {
            Integer page = Integer.parseInt(requestContext.getRequestParameters().get(RequestConstant.PAGE));
            try {
                List<Order> orders = orderService.findAll(page, PER_PAGE);
                Long count = orderService.countOrders();
                Pageable<Order> pageable = new Pageable<>(orders, page, count, PER_PAGE);
                return new ResponseContext(new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.ORDERS),
                        Map.of(RequestConstant.PAGEABLE, pageable), new HashMap<>());
            } catch (ServiceException e) {
                log.error("Failed to return all orders");
            }
        }
        return CommandType.ERROR.getCommand().execute(requestContext);
    }
}
