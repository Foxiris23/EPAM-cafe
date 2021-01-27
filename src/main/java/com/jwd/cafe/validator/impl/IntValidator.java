package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.AbstractValidator;
import com.jwd.cafe.validator.Validator;

import java.util.HashSet;
import java.util.Set;

public class IntValidator extends AbstractValidator {
    private final String requestConstant;

    public IntValidator(Validator next, String requestConstant) {
        super(next);
        this.requestConstant = requestConstant;
    }

    @Override
    public Set<String> validate(RequestContext context) {
        Set<String> violationMessages = new HashSet<>();
        try {
            Long.parseLong(context.getRequestParameters().get(requestConstant));
        } catch (NumberFormatException e) {
            violationMessages.add(
                    LocalizationHelper.localize(context.getLocale(), "violationMessage.incorrectInt"));
        }

        if (next != null) {
            violationMessages.addAll(next.validate(context));
        }

        return violationMessages;
    }
}