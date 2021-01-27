package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.AbstractValidator;
import com.jwd.cafe.validator.Validator;
import com.mysql.cj.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class EmailValidator extends AbstractValidator {
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public EmailValidator(Validator next) {
        super(next);
    }

    @Override
    public Set<String> validate(RequestContext context) {
        Set<String> violationMessages = new HashSet<>();
        String email = context.getRequestParameters().get(RequestConstant.EMAIL);

        if (StringUtils.isNullOrEmpty(email) || !email.matches(EMAIL_PATTERN)) {
            violationMessages.add(LocalizationHelper.localize(context.getLocale(), "violationMessage.email"));
        }

        if (next != null) {
            violationMessages.addAll(next.validate(context));
        }

        return violationMessages;
    }
}
