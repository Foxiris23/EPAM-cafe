package com.jwd.cafe.command;

import com.jwd.cafe.constant.RequestConstant;

public class RedirectResponse extends ResponseType {
    private final String command;

    public RedirectResponse(Type type, String command) {
        super(type);
        this.command = command;
    }

    public String getCommand() {
        return command.startsWith("?") ? command : "?" + RequestConstant.COMMAND + "=" + command;
    }
}
