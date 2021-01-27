package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.AbstractValidator;
import com.jwd.cafe.validator.Validator;
import com.mysql.cj.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class ProductNameValidator extends AbstractValidator {
    private static final String NAME_PATTERN = "^[A-Za-zа-я-А-я\\s'-]{4,20}?$";

    public ProductNameValidator(Validator next) {
        super(next);
    }

    @Override
    public Set<String> validate(RequestContext context) {
        Set<String> violationMessages = new HashSet<>();
        String typeName = context.getRequestParameters().get(RequestConstant.PRODUCT_NAME);

        if (StringUtils.isNullOrEmpty(typeName) || !typeName.matches(NAME_PATTERN)) {
            violationMessages.add(LocalizationHelper.localize(context.getLocale(), "violationMessage.productName"));
        }

        if (next != null) {
            violationMessages.addAll(next.validate(context));
        }

        return violationMessages;
    }
}
