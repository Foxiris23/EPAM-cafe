package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;

import java.util.HashMap;

/**
 * Removes {@link com.jwd.cafe.domain.User} from the {@link javax.servlet.http.HttpSession}
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public class LogoutCommand implements Command {

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return new ResponseContext(new RedirectResponse(ResponseType.Type.REDIRECT, CommandType.TO_MAIN.getName()),
                new HashMap<>(), new HashMap<>());
    }
}
