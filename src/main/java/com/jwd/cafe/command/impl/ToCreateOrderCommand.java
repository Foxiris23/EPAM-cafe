package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.PageConstant;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.Product;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.ProductService;
import com.jwd.cafe.util.JsonUtil;
import com.jwd.cafe.validator.impl.StringValidator;
import lombok.extern.log4j.Log4j2;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Log4j2
public class ToCreateOrderCommand implements Command {
    public final ProductService productService;

    public ToCreateOrderCommand() {
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
                if (productsMap.size() > 0) {
                    return new ResponseContext(new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.CREATE_ORDER),
                            Map.of(RequestConstant.TOTAL_COST, productService.calcTotalCost(productsMap)),
                            new HashMap<>());
                }
            } catch (JSONException | NumberFormatException | ServiceException e) {
                log.error("Failed to execute to-create-order command");
            }
        }
        return CommandType.ERROR.getCommand().execute(requestContext);
    }
}