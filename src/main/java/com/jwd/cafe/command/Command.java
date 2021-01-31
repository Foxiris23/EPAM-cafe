package com.jwd.cafe.command;

/**
 * The interface processes the client's {@link RequestContext} using the Command pattern
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public interface Command {
    ResponseContext execute(RequestContext requestContext);
}
