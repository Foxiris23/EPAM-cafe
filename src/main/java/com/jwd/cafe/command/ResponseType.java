package com.jwd.cafe.command;

import lombok.EqualsAndHashCode;

/**
 * Indicates the type of the {@link ResponseContext}
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@EqualsAndHashCode
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