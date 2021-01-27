package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.AbstractValidator;
import com.jwd.cafe.validator.Validator;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Part;
import java.util.HashSet;
import java.util.Set;

public class ImgFileValidator extends AbstractValidator {

    public ImgFileValidator(Validator next) {
        super(next);
    }

    @Override
    public Set<String> validate(RequestContext context) {
        Set<String> violationMessages = new HashSet<>();
        Part img = context.getRequestParts().get(RequestConstant.IMG_FILE);
        if (img == null || StringUtils.isEmpty(img.getSubmittedFileName()) ||
                (!img.getSubmittedFileName().endsWith(".png") && !img.getSubmittedFileName().endsWith(".jpg"))) {
            violationMessages.add(
                    LocalizationHelper.localize(context.getLocale(), "violationMessage.incorrectImg"));
        }

        if (next != null) {
            violationMessages.addAll(next.validate(context));
        }

        return violationMessages;
    }
}