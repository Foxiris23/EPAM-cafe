package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.Command;
import com.jwd.cafe.command.CommandType;
import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.command.ResponseContext;
import com.jwd.cafe.constant.RequestConstant;

import java.util.HashMap;
import java.util.Map;

public class RestErrorCommand implements Command {
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        return new ResponseContext(Map.of(RequestConstant.REDIRECT_COMMAND, CommandType.ERROR.getName()), new HashMap<>());
    }
}
