package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.validator.impl.LocaleValidator;
import com.mysql.cj.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LocaleSwitchCommand implements Command {
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages = new LocaleValidator(null).validate(requestContext);
        if (violationMessages.isEmpty()) {
            String locale = requestContext.getRequestParameters().get(RequestConstant.LOCALE);
            String currPage = requestContext.getRequestParameters().get(RequestConstant.CURRENT_PAGE);
            if (!StringUtils.isNullOrEmpty(currPage)) {
                return new ResponseContext(new RedirectResponse(ResponseType.Type.REDIRECT, currPage),
                        new HashMap<>(), Map.of(RequestConstant.LOCALE, locale));
            }
        }
        return CommandType.ERROR.getCommand().execute(requestContext);
    }
}
