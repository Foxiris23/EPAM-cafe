package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.ProductType;
import com.jwd.cafe.domain.User;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.UserService;
import com.jwd.cafe.validator.impl.NameValidator;
import com.jwd.cafe.validator.impl.PhoneNumberValidator;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Changes a {@link User}
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class RestEditProfileCommand implements Command {
    private final UserService userService;

    public RestEditProfileCommand(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     * executes {@link RestErrorCommand} if {@link ServiceException} was caught
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages = new NameValidator(
                new PhoneNumberValidator(null)).validate(requestContext);

        if (violationMessages.isEmpty()) {
            User user = (User) requestContext.getSessionAttributes().get(RequestConstant.USER);
            user.setFirstName(requestContext.getRequestParameters().get(RequestConstant.FIRST_NAME));
            user.setLastName(requestContext.getRequestParameters().get(RequestConstant.LAST_NAME));
            try {
                userService.updateUser(user);
                return new ResponseContext(Map.of(RequestConstant.REDIRECT_COMMAND, CommandType.USER_TO_PROFILE.getName()),
                        new HashMap<>());
            } catch (ServiceException e) {
                log.error("Failed to execute edit profile command");
                return RestCommandType.ERROR.getCommand().execute(requestContext);
            }
        }
        return new ResponseContext(Map.of(RequestConstant.VIOLATION_MESSAGE, violationMessages), new HashMap<>());
    }
}
