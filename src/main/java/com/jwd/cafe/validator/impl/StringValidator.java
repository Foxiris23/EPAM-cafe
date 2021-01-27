package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.AbstractValidator;
import com.jwd.cafe.validator.Validator;
import com.mysql.cj.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class StringValidator extends AbstractValidator {
    private final String requestConstant;

    public StringValidator(Validator next, String requestConstant) {
        super(next);
        this.requestConstant = requestConstant;
    }

    @Override
    public Set<String> validate(RequestContext context) {
        Set<String> violationMessages = new HashSet<>();
        String parameter = context.getRequestParameters().get(requestConstant).trim();

        if (StringUtils.isNullOrEmpty(parameter) || parameter.length() > 2048) {
            violationMessages.add(LocalizationHelper.localize(context.getLocale(), "violationMessage.emptyString"));
        }

        if (next != null) {
            violationMessages.addAll(next.validate(context));
        }

        return violationMessages;
    }
}
