package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;

import java.util.HashMap;

public class LogoutCommand implements Command{

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return new ResponseContext(new RedirectResponse(ResponseType.Type.REDIRECT, CommandType.TO_MAIN.getName()),
                new HashMap<>(), new HashMap<>());
    }
}
