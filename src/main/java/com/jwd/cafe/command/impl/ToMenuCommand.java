package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.PageConstant;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.ProductTypeService;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ToMenuCommand implements Command{
    private final ProductTypeService productTypeService;

    public ToMenuCommand(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        try {
            return new ResponseContext(new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.MENU_PAGE),
                    Map.of(RequestConstant.MENU_ITEMS, productTypeService.findAllProductTypes()), new HashMap<>());
        } catch (ServiceException e) {
            log.error("ProductService provided an exception", e);
            return CommandType.ERROR.getCommand().execute(requestContext);
        }
    }
}