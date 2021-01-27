package com.jwd.cafe.validator;

import com.jwd.cafe.command.RequestContext;

import java.util.Set;

public interface Validator {
    Set<String> validate(RequestContext context);
}
