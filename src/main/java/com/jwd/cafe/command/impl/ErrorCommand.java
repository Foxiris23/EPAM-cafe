package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.PageConstant;
import com.jwd.cafe.exception.ServiceException;

import java.util.HashMap;

/**
 * Redirects on the error page
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public class ErrorCommand implements Command{

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return new ResponseContext(new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.ERROR_PAGE),
                new HashMap<>(), new HashMap<>());
    }
}