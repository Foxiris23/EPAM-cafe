package com.jwd.cafe.command;

import com.jwd.cafe.constant.RequestConstant;

public class RestCommandFactory {
    private static final RestCommandFactory INSTANCE = new RestCommandFactory();

    public static RestCommandFactory getInstance() {
        return INSTANCE;
    }

    private RestCommandFactory() {
    }

    public Command getCommand(RequestContext requestContext) {
        String commandName = requestContext.getRequestParameters().get(RequestConstant.COMMAND);
        return RestCommandType.getCommandByName(commandName);
    }
}