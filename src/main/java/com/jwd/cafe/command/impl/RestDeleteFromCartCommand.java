package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.Command;
import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.command.ResponseContext;
import com.jwd.cafe.command.RestCommandType;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.ProductService;
import com.jwd.cafe.validator.impl.IntValidator;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Log4j2
public class RestDeleteFromCartCommand implements Command {
    private final ProductService productService;

    public RestDeleteFromCartCommand(ProductService productDao) {
        this.productService = productDao;
    }

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages = new IntValidator(null, RequestConstant.ID).validate(requestContext);
        if (violationMessages.isEmpty()) {
            Integer id = Integer.parseInt(requestContext.getRequestParameters().get(RequestConstant.ID));
            try {
                Map<Integer, Integer> cart =
                        (Map<Integer, Integer>) requestContext.getSessionAttributes().get(RequestConstant.CART);
                if (cart != null) {
                    if (productService.findProductById(id).isPresent()) {
                        if (cart.containsKey(id)) {
                            cart.put(id, cart.get(id) - 1);
                            if (cart.get(id) < 1) {
                                cart.remove(id);
                            }
                        }
                        return new ResponseContext(new HashMap<>(), new HashMap<>());
                    }
                }
            } catch (ServiceException e) {
                log.error("Failed to delete a product with id: " + id + " to cart");
            }
        }
        return RestCommandType.ERROR.getCommand().execute(requestContext);
    }
}
