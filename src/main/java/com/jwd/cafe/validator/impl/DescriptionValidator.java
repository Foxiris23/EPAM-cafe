package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.AbstractValidator;
import com.jwd.cafe.validator.Validator;
import com.mysql.cj.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class DescriptionValidator extends AbstractValidator {

    public DescriptionValidator(final Validator next) {
        super(next);
    }

    @Override
    public Set<String> validate(final RequestContext context) {
        Set<String> violationMessages = new HashSet<>();
        String composition = context.getRequestParameters().get(RequestConstant.DESCRIPTION);

        if (StringUtils.isNullOrEmpty(composition) || composition.length() < 5 || composition.length() > 40) {
            violationMessages.add(
                    LocalizationHelper.localize(context.getLocale(), "violationMessage.composition"));
        }


        if (next != null) {
            violationMessages.addAll(next.validate(context));
        }

        return violationMessages;
    }
}
