package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.AbstractValidator;
import com.jwd.cafe.validator.Validator;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class ReviewCodeValidator extends AbstractValidator {
    private static final String CODE_PATTERN = "^[A-Za-z0-9]{10}$";

    public ReviewCodeValidator(Validator next) {
        super(next);
    }

    @Override
    public Set<String> validate(RequestContext context) {
        Set<String> violationMessages = new HashSet<>();
        String code = context.getRequestParameters().get(RequestConstant.CODE);

        if(StringUtils.isEmpty(code) || !code.matches(CODE_PATTERN)){
            violationMessages.add(
                    LocalizationHelper.localize(context.getLocale(), "violationMessage.reviewCode"));
        }

        if (next != null) {
            violationMessages.addAll(next.validate(context));
        }

        return violationMessages;
    }
}
