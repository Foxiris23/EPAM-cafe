package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.AbstractValidator;
import com.jwd.cafe.validator.Validator;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class DateTimeValidator extends AbstractValidator {
    public DateTimeValidator(Validator next) {
        super(next);
    }

    @Override
    public Set<String> validate(RequestContext context) {
        Set<String> violationMessages = new HashSet<>();
        String timeStr = context.getRequestParameters().get(RequestConstant.TIME);
        if (StringUtils.isEmpty(timeStr)) {
            violationMessages.add(LocalizationHelper.localize(context.getLocale(), "violationMessage.incorrectTime"));
        } else {
            try {
                LocalDateTime dateTime =
                        LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern(RequestConstant.TIME_PATTERN));
                if (dateTime.compareTo(LocalDateTime.now().plusHours(1)) < 1) {
                    throw new Exception();
                }
            } catch (Exception e) {
                violationMessages.add(LocalizationHelper.localize(
                        context.getLocale(), "violationMessage.incorrectTimeAndDate"));
            }
        }

        if (next != null) {
            violationMessages.addAll(next.validate(context));
        }

        return violationMessages;
    }
}
