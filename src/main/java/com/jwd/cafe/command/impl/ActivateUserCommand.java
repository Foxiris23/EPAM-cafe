package com.jwd.cafe.command.impl;


import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.PageConstant;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.UserService;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.impl.StringValidator;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Activates {@link com.jwd.cafe.domain.User}
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class ActivateUserCommand implements Command {
    private final UserService userService;

    public ActivateUserCommand(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     * executes {@link ErrorCommand} if {@link ServiceException} was caught
     */
    @Override
    public ResponseContext execute(final RequestContext requestContext) {
        Set<String> violationMessages =
                new StringValidator(null, RequestConstant.VERIFICATION_CODE).validate(requestContext);
        if (violationMessages.isEmpty()) {
            String activationCode = requestContext.getRequestParameters().get(RequestConstant.VERIFICATION_CODE);
            try {
                Optional<String> serverMessage = userService.activateUser(activationCode);
                if (serverMessage.isEmpty()) {
                    return new ResponseContext(
                            new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.LOGIN_PAGE),
                            Map.of(RequestConstant.VERIFICATION_MESSAGE, LocalizationHelper.localize(
                                    requestContext.getLocale(), "verificationMessage.verificationSuccess")),
                            new HashMap<>());
                }
                return new ResponseContext(new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.LOGIN_PAGE),
                        Map.of(
                        RequestConstant.SERVER_MESSAGE, LocalizationHelper.localize(
                                requestContext.getLocale(), serverMessage.get())), new HashMap<>());
            } catch (ServiceException e) {
                log.error("Failed to activate user", e);
                return CommandType.ERROR.getCommand().execute(requestContext);
            }
        }
        return new ResponseContext(new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.LOGIN_PAGE),
                Map.of(RequestConstant.VIOLATION_MESSAGE, violationMessages), new HashMap<>());
    }
}
