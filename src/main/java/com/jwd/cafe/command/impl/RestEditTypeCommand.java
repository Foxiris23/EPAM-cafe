package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.ProductTypeService;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.impl.ImgFileValidator;
import com.jwd.cafe.validator.impl.IntValidator;
import com.jwd.cafe.validator.impl.ProductNameValidator;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.*;

@Log4j2
public class RestEditTypeCommand implements Command{
    public final ProductTypeService productTypeService;

    public RestEditTypeCommand() {
        productTypeService = ProductTypeService.getInstance();
    }

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages =
                new ProductNameValidator(
                        new ImgFileValidator(
                                new IntValidator(null, RequestConstant.ID))).validate(requestContext);

        if (violationMessages.isEmpty()) {
            try {
                String newName = requestContext.getRequestParameters().get(RequestConstant.PRODUCT_NAME);
                Part newImg = requestContext.getRequestParts().get(RequestConstant.IMG_FILE);
                String filename = UUID.randomUUID() + newImg.getSubmittedFileName();
                Integer typeId = Integer.parseInt(requestContext.getRequestParameters().get(RequestConstant.ID));
                Optional<String> serverMessage = productTypeService.editType(typeId, newName, filename);
                if (serverMessage.isEmpty()) {
                    newImg.write(filename);
                    return new ResponseContext(
                            Map.of(RequestConstant.REDIRECT_COMMAND, CommandType.TO_MENU.getName()),
                            new HashMap<>());
                }
                return new ResponseContext(Map.of(RequestConstant.SERVER_MESSAGE,
                        LocalizationHelper.localize(requestContext.getLocale(), serverMessage.get())),
                        new HashMap<>());
            } catch (ServiceException | IOException e) {
                log.error("Failed to execute edit type command");
            }
            return RestCommandType.ERROR.getCommand().execute(requestContext);
        }
        return new ResponseContext(Map.of(RequestConstant.VIOLATION_MESSAGE, violationMessages), new HashMap<>());
    }
}
