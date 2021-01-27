package com.jwd.cafe.command;

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
