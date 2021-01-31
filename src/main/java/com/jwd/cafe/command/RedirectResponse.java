package com.jwd.cafe.command;

import com.jwd.cafe.constant.RequestConstant;
import lombok.EqualsAndHashCode;

/**
 * Returns in the {@link ResponseContext} to make a {@link com.jwd.cafe.controller.Controller} do a http redirect
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
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
