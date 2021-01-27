package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.PageConstant;

import java.util.HashMap;

public class ToOrderConfirmCommand implements Command{
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return new ResponseContext(new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.ORDER_CONFIRM),
                new HashMap<>(), new HashMap<>());
    }
}
