package com.jwd.cafe.command;

import lombok.EqualsAndHashCode;

/**
 * Returns in the {@link ResponseContext} to make a {@link com.jwd.cafe.controller.Controller} do a http forward
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
public class ForwardResponse extends ResponseType {
    private final String page;

    public ForwardResponse(Type type, String page) {
        super(type);
        this.page = page;
    }

    public String getPage() {
        return page;
    }
}
