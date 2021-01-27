package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.ProductType;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.ProductTypeService;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.impl.ImgFileValidator;
import com.jwd.cafe.validator.impl.ProductNameValidator;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.*;

@Log4j2
public class RestAddTypeCommand implements Command{
    private final ProductTypeService productTypeService;

    public RestAddTypeCommand(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages = new ImgFileValidator(new ProductNameValidator(null)).validate(requestContext);

        if (violationMessages.isEmpty()) {
            Part img = requestContext.getRequestParts().get(RequestConstant.IMG_FILE);
            String filename = UUID.randomUUID() + img.getSubmittedFileName();
            String type_name = requestContext.getRequestParameters().get(RequestConstant.PRODUCT_NAME);
            try {
                Optional<String> serverMessage = productTypeService.createType(
                        ProductType.builder().withName(type_name).withFilename(filename).build());
                if (serverMessage.isEmpty()) {
                    img.write(filename);
                    return new ResponseContext(
                            Map.of(RequestConstant.REDIRECT_COMMAND, CommandType.TO_MENU.getName()), new HashMap<>());
                }
                return new ResponseContext(Map.of(RequestConstant.SERVER_MESSAGE,
                        LocalizationHelper.localize(requestContext.getLocale(), serverMessage.get())), new HashMap<>());
            } catch (ServiceException e) {
                log.error("ProductService provided an exception", e);
            } catch (IOException e) {
                log.error("Failed to save upload image with name: " + filename, e);
            }
            return RestCommandType.ERROR.getCommand().execute(requestContext);
        }

        return new ResponseContext(Map.of(RequestConstant.VIOLATION_MESSAGE, violationMessages), new HashMap<>());
    }
}
