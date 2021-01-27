package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.AbstractValidator;
import com.jwd.cafe.validator.Validator;
import com.mysql.cj.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class PhoneNumberValidator extends AbstractValidator {
    private static final String NUMBER_PATTERN = "^\\+375((44)|(33)|(29))[0-9]{7}$";

    public PhoneNumberValidator(Validator next) {
        super(next);
    }

    @Override
    public Set<String> validate(RequestContext context) {
        Set<String> violationMessages = new HashSet<>();
        String number = context.getRequestParameters().get(RequestConstant.PHONE_NUMBER);

        if (StringUtils.isNullOrEmpty(number) || !number.matches(NUMBER_PATTERN)) {
            violationMessages.add(LocalizationHelper.localize(context.getLocale(), "violationMessage.phoneNumber"));
        }

        if (next != null) {
            violationMessages.addAll(next.validate(context));
        }

        return violationMessages;
    }
}
