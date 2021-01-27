package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.AbstractValidator;
import com.jwd.cafe.validator.Validator;
import com.mysql.cj.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class PasswordRepeatValidator extends AbstractValidator {
    public PasswordRepeatValidator(Validator next) {
        super(next);
    }

    @Override
    public Set<String> validate(RequestContext context) {
        Set<String> violationMessages = new HashSet<>();
        String password = context.getRequestParameters().get(RequestConstant.PASSWORD);
        String passwordRepeat = context.getRequestParameters().get(RequestConstant.REPEAT_PASSWORD);
        if (StringUtils.isNullOrEmpty(password) || StringUtils.isNullOrEmpty(passwordRepeat)) {
            violationMessages.add(LocalizationHelper.localize(context.getLocale(), "violationMessage.password"));
        }

        if (!password.equals(passwordRepeat)) {
            violationMessages.add(LocalizationHelper.localize(context.getLocale(), "violationMessage.passwordRepeat"));
        }

        if (next != null) {
            violationMessages.addAll(next.validate(context));
        }

        return violationMessages;
    }
}
