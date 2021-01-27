package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.AbstractValidator;
import com.jwd.cafe.validator.Validator;
import com.mysql.cj.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class UsernameValidator extends AbstractValidator {
    private static final String USERNAME_PATTERN = "^[A-Za-z_]{4,20}$";
    private static final String USERNAME_VIOLATION_MESSAGE = "violationMessage.username";

    public UsernameValidator(Validator next) {
        super(next);
    }

    @Override
    public Set<String> validate(RequestContext context) {
        Set<String> violationMessages = new HashSet<>();

        String username = context.getRequestParameters().get("username");
        if (StringUtils.isNullOrEmpty(username) || !username.matches(USERNAME_PATTERN)) {
            violationMessages.add(LocalizationHelper.localize(context.getLocale(), "violationMessage.username"));
        }

        if (next != null) {
            violationMessages.addAll(next.validate(context));
        }

        return violationMessages;
    }
}
