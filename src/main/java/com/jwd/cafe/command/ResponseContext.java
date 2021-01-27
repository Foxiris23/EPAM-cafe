package com.jwd.cafe.command;

import lombok.Getter;

import java.util.Map;

@Getter
public class ResponseContext {
    private ResponseType responseType;
    private final Map<String, Object> requestAttributes;
    private final Map<String, Object> sessionAttributes;

    public ResponseContext(ResponseType responseType, Map<String, Object> requestAttributes, Map<String, Object> sessionAttributes
    ) {
        this.responseType = responseType;
        this.requestAttributes = requestAttributes;
        this.sessionAttributes = sessionAttributes;
    }

    public ResponseContext(Map<String, Object> requestAttributes, Map<String, Object> sessionAttributes) {
        this.requestAttributes = requestAttributes;
        this.sessionAttributes = sessionAttributes;
    }
}