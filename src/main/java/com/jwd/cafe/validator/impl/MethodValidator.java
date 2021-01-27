package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.PaymentMethod;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.AbstractValidator;
import com.jwd.cafe.validator.Validator;

import java.util.HashSet;
import java.util.Set;

public class MethodValidator extends AbstractValidator {
    public MethodValidator(Validator next) {
        super(next);
    }

    @Override
    public Set<String> validate(RequestContext context) {
        Set<String> violationMessages = new HashSet<>();
        String method = context.getRequestParameters().get(RequestConstant.METHOD);
        try {
            PaymentMethod.valueOf(method);
        } catch (Exception e) {
            violationMessages.add(
                    LocalizationHelper.localize(context.getLocale(), "violationMessage.method"));
        }

        if (next != null) {
            violationMessages.addAll(next.validate(context));
        }

        return violationMessages;
    }
}
