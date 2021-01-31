package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.Command;
import com.jwd.cafe.command.CommandType;
import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.command.ResponseContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.exception.ServiceException;

import java.util.HashMap;
import java.util.Map;

/**
 * Redirects on the error page
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public class RestErrorCommand implements Command {

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     * executes {@link RestErrorCommand} if {@link ServiceException} was caught
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return new ResponseContext(Map.of(RequestConstant.REDIRECT_COMMAND, CommandType.ERROR.getName()), new HashMap<>());
    }
}
