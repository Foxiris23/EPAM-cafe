package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.OrderStatus;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.AbstractValidator;
import com.jwd.cafe.validator.Validator;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class OrderStatusValidator extends AbstractValidator {
    public OrderStatusValidator(Validator next) {
        super(next);
    }

    @Override
    public Set<String> validate(RequestContext context) {
        Set<String> violationMessages = new HashSet<>();
        String statusName = context.getRequestParameters().get(RequestConstant.SELECT);

        if (!StringUtils.isEmpty(statusName)) {
            try {
                OrderStatus orderStatus = OrderStatus.valueOf(statusName);
            } catch (Exception e) {
                violationMessages.add(
                        LocalizationHelper.localize(context.getLocale(), "violationMessage.status"));
            }
        } else {
            violationMessages.add(
                    LocalizationHelper.localize(context.getLocale(), "violationMessage.emptyStatus"));
        }


        if (next != null) {
            violationMessages.addAll(next.validate(context));
        }

        return violationMessages;
    }
}
