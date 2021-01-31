package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.Command;
import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.command.ResponseContext;
import com.jwd.cafe.command.RestCommandType;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.Product;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.ProductService;
import com.jwd.cafe.validator.impl.IntValidator;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Adds a {@link Product} to {@link com.jwd.cafe.domain.User}'s cart
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class RestAddToCartCommand implements Command {
    private final ProductService productService;

    public RestAddToCartCommand(ProductService productDao) {
        this.productService = productDao;
    }

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     * executes {@link RestErrorCommand} if {@link ServiceException} was caught
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages = new IntValidator(null, RequestConstant.ID).validate(requestContext);
        if (violationMessages.isEmpty()) {
            Integer id = Integer.parseInt(requestContext.getRequestParameters().get(RequestConstant.ID));
            try {
                if (productService.findProductById(id).isPresent()) {
                    Map<String, Object> sessionResponse = new HashMap<>();
                    Map<Integer, Integer> cart =
                            (Map<Integer, Integer>) requestContext.getSessionAttributes().get(RequestConstant.CART);
                    if (cart == null) {
                        cart = new HashMap<>();
                        sessionResponse.put(RequestConstant.CART, cart);
                    }
                    if (cart.containsKey(id)) {
                        cart.put(id, cart.get(id) + 1);
                    } else {
                        cart.put(id, 1);
                    }
                    return new ResponseContext(new HashMap<>(), sessionResponse);
                }
            } catch (ServiceException e) {
                log.error("Failed to add a product with id: " + id + " to cart");
            }
        }
        return RestCommandType.ERROR.getCommand().execute(requestContext);
    }
}
