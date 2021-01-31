package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.PageConstant;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.Product;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.ProductService;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

/**
 * Moves an user to createOrder page
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class ToCreateOrderCommand implements Command {
    private final ProductService productService;

    public ToCreateOrderCommand(ProductService productService) {
        this.productService = productService;
    }

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     * executes {@link ErrorCommand} if {@link ServiceException} was caught
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
            try {
                Map<Integer, Integer> cart =
                        (Map<Integer, Integer>) requestContext.getSessionAttributes().get(RequestConstant.CART);
                if (cart != null) {
                    Map<Product, Integer> productsMap = productService.loadCart(cart);
                    if (productsMap.size() > 0) {
                        return new ResponseContext(new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.CREATE_ORDER),
                                Map.of(RequestConstant.TOTAL_COST, productService.calcTotalCost(productsMap)),
                                new HashMap<>());
                    }
                }
            } catch (ServiceException e) {
                log.error("Failed to execute to-create-order command");
            }
        return CommandType.ERROR.getCommand().execute(requestContext);
    }
}