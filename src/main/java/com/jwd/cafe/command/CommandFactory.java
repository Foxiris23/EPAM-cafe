package com.jwd.cafe.command;

import com.jwd.cafe.constant.RequestConstant;

public class CommandFactory {
    private static final CommandFactory INSTANCE = new CommandFactory();

    public static CommandFactory getInstance() {
        return INSTANCE;
    }

    private CommandFactory() {
    }

    public Command getCommand(RequestContext requestContext) {
        String commandName = requestContext.getRequestParameters().get(RequestConstant.COMMAND);
        return CommandType.getCommandByName(commandName);
    }
}