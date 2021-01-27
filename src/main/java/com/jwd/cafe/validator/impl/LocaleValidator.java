package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.AbstractValidator;
import com.jwd.cafe.validator.Validator;
import com.mysql.cj.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LocaleValidator extends AbstractValidator {
    enum Locale {
        ru_RU, be_BY, en_US;

        public static Boolean isAvailable(String name) {
            return Arrays.stream(Locale.values()).anyMatch(locale -> locale.name().equals(name));
        }
    }

    public LocaleValidator(Validator next) {
        super(next);
    }

    @Override
    public Set<String> validate(RequestContext context) {
        Set<String> violationMessages = new HashSet<>();
        String locale = context.getRequestParameters().get(RequestConstant.LOCALE);

        if (StringUtils.isNullOrEmpty(locale) || !Locale.isAvailable(locale)) {
            violationMessages.add(LocalizationHelper.localize(context.getLocale(), "serverMessage.incorrectLocale"));
        }

        if (next != null) {
            violationMessages.addAll(next.validate(context));
        }

        return violationMessages;
    }
}
