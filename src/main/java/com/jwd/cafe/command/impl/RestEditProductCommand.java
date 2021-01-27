package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.Command;
import com.jwd.cafe.command.CommandType;
import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.command.ResponseContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.ProductService;
import com.jwd.cafe.validator.impl.DescriptionValidator;
import com.jwd.cafe.validator.impl.IntValidator;
import com.jwd.cafe.validator.impl.PriceValidator;
import com.jwd.cafe.validator.impl.ProductNameValidator;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Log4j2
public class RestEditProductCommand implements Command {
    public final ProductService productService;

    public RestEditProductCommand() {
        productService = ProductService.getInstance();
    }

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages =
                new PriceValidator(new ProductNameValidator(
                        new DescriptionValidator(new IntValidator(null, RequestConstant.ID))))
                        .validate(requestContext);
        if (violationMessages.isEmpty()) {
            String name = requestContext.getRequestParameters().get(RequestConstant.PRODUCT_NAME);
            String description = requestContext.getRequestParameters().get(RequestConstant.DESCRIPTION);
            Double price = Double.parseDouble(requestContext.getRequestParameters().get(RequestConstant.PRICE));
            Integer id = Integer.parseInt(requestContext.getRequestParameters().get(RequestConstant.ID));
            try {
                Optional<String> serverMessage = productService.editProduct(id, name, price, description);
                if (serverMessage.isEmpty()) {
                    return new ResponseContext(Map.of(RequestConstant.REDIRECT_COMMAND, CommandType.TO_MENU_ITEM),
                            new HashMap<>());
                }
                return new ResponseContext(Map.of(RequestConstant.SERVER_MESSAGE, serverMessage.get()),
                        new HashMap<>());
            } catch (ServiceException e) {
                log.error("Failed to execute edit product command", e);
            }
        }
        return new ResponseContext(Map.of(RequestConstant.VIOLATION_MESSAGE, violationMessages), new HashMap<>());
    }
}