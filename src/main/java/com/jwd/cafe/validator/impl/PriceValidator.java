package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.AbstractValidator;
import com.jwd.cafe.validator.Validator;
import com.mysql.cj.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class PriceValidator extends AbstractValidator {
    private static final String PRICE_PATTERN = "^([0-9]{1,3}.[0-9]{1,2}|[0-9]{1,2})$";

    public PriceValidator(Validator next) {
        super(next);
    }

    @Override
    public Set<String> validate(RequestContext context) {
        Set<String> violationMessages = new HashSet<>();
        String price = context.getRequestParameters().get(RequestConstant.PRICE);

        if (StringUtils.isNullOrEmpty(price) || !price.matches(PRICE_PATTERN)) {
            violationMessages.add(
                    LocalizationHelper.localize(context.getLocale(), "violationMessage.price"));
        }


        if (next != null) {
            violationMessages.addAll(next.validate(context));
        }

        return violationMessages;
    }
}
