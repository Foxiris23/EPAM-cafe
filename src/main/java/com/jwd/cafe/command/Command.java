package com.jwd.cafe.command;

public interface Command {
    ResponseContext execute(RequestContext requestContext);
}
