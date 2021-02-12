package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.PageConstant;

import java.util.HashMap;

public class PageNotFoundCommand implements Command {
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return new ResponseContext(new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.ERROR_404),
                new HashMap<>(), new HashMap<>());
    }
}
