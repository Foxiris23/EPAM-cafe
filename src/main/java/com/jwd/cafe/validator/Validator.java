package com.jwd.cafe.validator;

import com.jwd.cafe.command.RequestContext;

import java.util.Set;

/**
 * The interface of application validator
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public interface Validator {
    Set<String> validate(RequestContext context);
}
