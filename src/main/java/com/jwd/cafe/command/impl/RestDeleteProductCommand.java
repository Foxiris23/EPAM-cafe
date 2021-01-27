package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.Command;
import com.jwd.cafe.command.CommandType;
import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.command.ResponseContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.ProductService;
import com.jwd.cafe.validator.impl.IntValidator;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Log4j2
public class RestDeleteProductCommand implements Command {
    private final ProductService productService;

    public RestDeleteProductCommand() {
        productService = ProductService.getInstance();
    }

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages =
                new IntValidator(null, RequestConstant.ID).validate(requestContext);

        if (violationMessages.isEmpty()) {
            Integer id = Integer.parseInt(requestContext.getRequestParameters().get(RequestConstant.ID));
            try {
                productService.delete(id);
                return new ResponseContext(
                        Map.of(RequestConstant.REDIRECT_COMMAND, CommandType.TO_MENU_ITEM.getName()),
                        new HashMap<>());
            } catch (ServiceException e) {
                log.error("Failed to execute delete product command", e);
            }
        }
        return new ResponseContext(Map.of(RequestConstant.VIOLATION_MESSAGE, violationMessages), new HashMap<>());
    }
}
