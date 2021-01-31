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
 * Moves an user to cart page
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class ToCartCommand implements Command{
    private final ProductService productService;

    public ToCartCommand(ProductService productService) {
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
                Map<Product, Integer> productsMap;
                if(cart!=null) {
                    productsMap = productService.loadCart(cart);
                }else{
                    productsMap = new HashMap<>();
                }
                return new ResponseContext(new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.CART),
                        Map.of(RequestConstant.CART, productsMap.entrySet()), new HashMap<>());
            } catch (ServiceException e) {
                log.error("Failed to execute to-cart command");
            }
        return CommandType.ERROR.getCommand().execute(requestContext);
    }
}
