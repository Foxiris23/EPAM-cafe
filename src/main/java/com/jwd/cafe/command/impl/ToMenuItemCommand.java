package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.PageConstant;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.Product;
import com.jwd.cafe.domain.ProductType;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.ProductService;
import com.jwd.cafe.service.ProductTypeService;
import com.jwd.cafe.util.Pageable;
import com.jwd.cafe.validator.impl.IntValidator;
import lombok.extern.log4j.Log4j2;

import java.util.*;

/**
 * Moves an user to menuItem page
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class ToMenuItemCommand implements Command {
    private static final Integer PER_PAGE = 9;
    private final ProductTypeService productTypeService;
    private final ProductService productService;

    public ToMenuItemCommand(ProductTypeService productTypeService, ProductService productService) {
        this.productTypeService = productTypeService;
        this.productService = productService;
    }

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     * executes {@link ErrorCommand} if {@link ServiceException} was caught
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages = new IntValidator(new IntValidator(null, RequestConstant.PAGE),
                RequestConstant.TYPE_ID).validate(requestContext);

        if (violationMessages.isEmpty()) {
            try {
                int page = Integer.parseInt(requestContext.getRequestParameters().get(RequestConstant.PAGE));
                int type_id = Integer.parseInt(requestContext.getRequestParameters().get(RequestConstant.TYPE_ID));
                if (page < 1) {
                    page = 1;
                }
                Optional<ProductType> optional = productTypeService.findProductTypeById(type_id);
                if (optional.isPresent()) {
                    ProductType productType = optional.get();
                    Long count = productService.countProductWithTypeId(productType.getId());
                    List<Product> products = productService.findProductsByTypeId(productType.getId(), page, PER_PAGE);
                    Pageable<Product> pageable = new Pageable<>(products, page, count, PER_PAGE);
                    return new ResponseContext(new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.PRODUCTS),
                            Map.of(RequestConstant.PAGEABLE, pageable,
                                    RequestConstant.PRODUCT_TYPE, productType), new HashMap<>());
                }
            } catch (ArithmeticException | ServiceException e) {
                log.error("Moving to menu item has failed", e);
            }
        }
        return CommandType.ERROR.getCommand().execute(requestContext);
    }
}
