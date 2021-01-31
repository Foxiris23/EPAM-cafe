package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.Product;
import com.jwd.cafe.domain.ProductType;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.ProductService;
import com.jwd.cafe.service.ProductTypeService;
import com.jwd.cafe.validator.impl.DescriptionValidator;
import com.jwd.cafe.validator.impl.ImgFileValidator;
import com.jwd.cafe.validator.impl.PriceValidator;
import com.jwd.cafe.validator.impl.ProductNameValidator;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.*;

/**
 * Creates a new {@link Product}
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class RestAddProductCommand implements Command {
    private final ProductTypeService productTypeService;
    private final ProductService productService;

    public RestAddProductCommand(ProductTypeService productTypeService, ProductService productService) {
        this.productTypeService = productTypeService;
        this.productService = productService;
    }

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     * executes {@link RestErrorCommand} if {@link ServiceException} was caught
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages = new ProductNameValidator(
                new PriceValidator(new DescriptionValidator(new ImgFileValidator(null)))).validate(requestContext);
        if (violationMessages.isEmpty()) {
            try {
                Part img = requestContext.getRequestParts().get(RequestConstant.IMG_FILE);
                String filename = UUID.randomUUID() + img.getSubmittedFileName();
                Integer type_id = Integer.parseInt(requestContext.getRequestParameters().get(RequestConstant.TYPE_ID));
                String name = requestContext.getRequestParameters().get(RequestConstant.PRODUCT_NAME);
                String description = requestContext.getRequestParameters().get(RequestConstant.DESCRIPTION);
                Double price = Double.parseDouble(requestContext.getRequestParameters().get(RequestConstant.PRICE));
                Optional<ProductType> productTypeOptional = productTypeService.findProductTypeById(type_id);
                if (productTypeOptional.isPresent()) {
                    Product product = Product.builder()
                            .withProductType(productTypeOptional.get()).withName(name)
                            .withDescription(description).withImgFilename(filename)
                            .withPrice(price).build();
                    Optional<String> serverMessage = productService.createProduct(product);
                    if (serverMessage.isEmpty()) {
                        img.write(filename);
                        return new ResponseContext(Map.of(
                                RequestConstant.REDIRECT_COMMAND, CommandType.TO_MENU_ITEM.getName(), RequestConstant.TYPE_ID, type_id),
                                new HashMap<>());
                    }
                    return new ResponseContext(Map.of(RequestConstant.SERVER_MESSAGE, serverMessage.get()), new HashMap<>());
                }
            } catch (NumberFormatException | ServiceException | IOException e) {
                log.error("Failed to create a product", e);
            }
        } else {
            return new ResponseContext(Map.of(RequestConstant.VIOLATION_MESSAGE, violationMessages), new HashMap<>());
        }
        return RestCommandType.ERROR.getCommand().execute(requestContext);
    }
}
