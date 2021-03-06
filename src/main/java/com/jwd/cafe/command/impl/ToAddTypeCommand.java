package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.PageConstant;
import com.jwd.cafe.exception.ServiceException;

import java.util.HashMap;

/**
 * Moves an admin to addProductType page
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public class ToAddTypeCommand implements Command{

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     * executes {@link ErrorCommand} if {@link ServiceException} was caught
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return new ResponseContext(new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.ADD_PRODUCT_TYPE_PAGE),
                new HashMap<>(), new HashMap<>());
    }
}
