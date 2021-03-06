package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.Command;
import com.jwd.cafe.command.CommandType;
import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.command.ResponseContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.ProductType;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.ProductTypeService;
import com.jwd.cafe.util.IOUtil;
import com.jwd.cafe.validator.impl.IntValidator;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Deletes a {@link ProductType}
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class RestDeleteTypeCommand implements Command {
    private final ProductTypeService productTypeService;

    public RestDeleteTypeCommand(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     * executes {@link RestErrorCommand} if {@link ServiceException} was caught
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages =
                new IntValidator(null, RequestConstant.ID).validate(requestContext);

        if (violationMessages.isEmpty()) {
            Integer id = Integer.parseInt(requestContext.getRequestParameters().get(RequestConstant.ID));
            try {
                Optional<ProductType> productTypeOptional = productTypeService.findProductTypeById(id);

                if (productTypeOptional.isPresent()) {
                    ProductType productType = productTypeOptional.get();
                    productTypeService.deleteTypeById(productType.getId());
                    IOUtil.deleteUpload(productType.getFilename());
                    return new ResponseContext(Map.of(RequestConstant.REDIRECT_COMMAND, CommandType.TO_MENU.getName()),
                            new HashMap<>());
                }
            } catch (ServiceException e) {
                log.error("Failed to execute delete type product");
            }
        }

        return new ResponseContext(Map.of(RequestConstant.VIOLATION_MESSAGE, violationMessages), new HashMap<>());
    }
}
