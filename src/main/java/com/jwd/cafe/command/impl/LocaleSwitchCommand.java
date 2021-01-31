package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.validator.impl.LocaleValidator;
import com.mysql.cj.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Changes current {@link com.jwd.cafe.domain.User}'s locale in the {@link javax.servlet.http.HttpSession}
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public class LocaleSwitchCommand implements Command {

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     * executes {@link ErrorCommand} if {@link ServiceException} was caught
     */
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
