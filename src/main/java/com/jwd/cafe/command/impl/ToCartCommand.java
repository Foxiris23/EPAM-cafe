package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.PageConstant;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.Product;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.ProductService;
import com.jwd.cafe.service.UserService;
import com.jwd.cafe.util.JsonUtil;
import com.jwd.cafe.validator.impl.StringValidator;
import lombok.extern.log4j.Log4j2;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Log4j2
public class ToCartCommand implements Command{
    public final ProductService productService;

    public ToCartCommand() {
        productService = ProductService.getInstance();
    }

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationsMessage =
                new StringValidator(null, RequestConstant.CART).validate(requestContext);

        if (violationsMessage.isEmpty()) {
            try {
                String json = requestContext.getRequestParameters().get(RequestConstant.CART);
                Map<Integer, Integer> cart = JsonUtil.jsonToCart(json);
                Map<Product, Integer> productsMap = productService.loadCart(cart);
                return new ResponseContext(new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.CART),
                        Map.of(RequestConstant.CART, productsMap.entrySet()), new HashMap<>());
            } catch (JSONException | NumberFormatException | ServiceException e) {
                log.error("Failed to execute to-cart command");
            }
        }
        return CommandType.ERROR.getCommand().execute(requestContext);
    }
}
