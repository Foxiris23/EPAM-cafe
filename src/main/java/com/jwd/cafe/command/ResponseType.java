package com.jwd.cafe.command;

public abstract class ResponseType {
    public enum Type {
        REDIRECT,
        FORWARD
    }

    private final Type type;

    public ResponseType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}