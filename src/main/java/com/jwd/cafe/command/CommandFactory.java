package com.jwd.cafe.command;

import com.jwd.cafe.constant.RequestConstant;

public class CommandFactory {
    private static volatile CommandFactory instance;

    public static CommandFactory getInstance() {
        CommandFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (CommandFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new CommandFactory();
                }
            }
        }
        return localInstance;
    }

    private CommandFactory() {
    }

    public Command getCommand(RequestContext requestContext) {
        String commandName = requestContext.getRequestParameters().get(RequestConstant.COMMAND);
        return CommandType.getCommandByName(commandName);
    }
}