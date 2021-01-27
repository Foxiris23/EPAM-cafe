package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
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

@Log4j2
public class RestLoginCommand implements Command{
    private final UserService userService;

    public RestLoginCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages =
                new StringValidator(
                        new StringValidator(null, RequestConstant.USERNAME),
                        RequestConstant.PASSWORD)
                        .validate(requestContext);

        if (violationMessages.isEmpty()) {
            String username = requestContext.getRequestParameters().get(RequestConstant.USERNAME);
            String password = requestContext.getRequestParameters().get(RequestConstant.PASSWORD);
            try {
                Map<String, Object> responseSession = new HashMap<>();
                Optional<String> serverMessage =
                        userService.login(username, password, responseSession);
                if (serverMessage.isEmpty()) {
                    return new ResponseContext(Map.of(RequestConstant.REDIRECT_COMMAND, CommandType.TO_MAIN.getName()),
                            responseSession);
                }
                return new ResponseContext(Map.of(RequestConstant.SERVER_MESSAGE,
                        LocalizationHelper.localize(requestContext.getLocale(), serverMessage.get())),
                        new HashMap<>());
            } catch (ServiceException e) {
                log.error("Processing of login command has failed", e);
                return RestCommandType.ERROR.getCommand().execute(requestContext);
            }
        }
        return new ResponseContext(
                Map.of(RequestConstant.VIOLATION_MESSAGE, violationMessages), new HashMap<>());
    }
}
