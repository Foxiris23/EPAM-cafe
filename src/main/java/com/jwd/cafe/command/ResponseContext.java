package com.jwd.cafe.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Is the result of the {@link Command} execution
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Getter
@EqualsAndHashCode
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