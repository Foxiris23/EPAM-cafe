package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.AbstractValidator;
import com.jwd.cafe.validator.Validator;
import com.mysql.cj.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class NameValidator extends AbstractValidator {
    private static final String NAME_PATTERN = "^[A-Za-zА-Яа-яЁё']{2,20}?$";

    public NameValidator(Validator next) {
        super(next);
    }

    @Override
    public Set<String> validate(RequestContext context) {
        Set<String> violationMessages = new HashSet<>();
        String firstName = context.getRequestParameters().get(RequestConstant.FIRST_NAME);
        String lastName = context.getRequestParameters().get(RequestConstant.LAST_NAME);

        if (StringUtils.isNullOrEmpty(firstName) || StringUtils.isNullOrEmpty(lastName)
                || !firstName.matches(NAME_PATTERN) || !lastName.matches(NAME_PATTERN)) {
            violationMessages.add(LocalizationHelper.localize(context.getLocale(), "violationMessage.name"));
        }

        if (next != null) {
            violationMessages.addAll(next.validate(context));
        }

        return violationMessages;
    }
}
