package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.Command;
import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.command.ResponseContext;
import com.jwd.cafe.command.RestCommandType;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.User;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.OrderService;
import com.jwd.cafe.service.UserService;
import com.jwd.cafe.validator.impl.IntValidator;
import com.jwd.cafe.validator.impl.StringValidator;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Allows admin to update an {@link com.jwd.cafe.domain.User}
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class RestUpdateUserCommand implements Command {
    private final UserService userService;

    public RestUpdateUserCommand(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     * executes {@link RestErrorCommand} if {@link ServiceException} was caught
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationsMessages = new IntValidator(
                new IntValidator(new StringValidator(null, RequestConstant.CHECK),
                        RequestConstant.POINTS), RequestConstant.ID).validate(requestContext);

        if (violationsMessages.isEmpty()) {
            try {
                Long userId = Long.parseLong(requestContext.getRequestParameters().get(RequestConstant.ID));
                Integer loyaltyPoints =
                        Integer.parseInt(requestContext.getRequestParameters().get(RequestConstant.POINTS));
                Boolean isBlocked =
                        requestContext.getRequestParameters().get(RequestConstant.CHECK).equals(RequestConstant.TRUE);
                Optional<User> userOptional = userService.findById(userId);
                if (userOptional.isEmpty()) {
                    return RestCommandType.ERROR.getCommand().execute(requestContext);
                }
                User user = userOptional.get();
                user.setLoyaltyPoints(loyaltyPoints);
                user.setIsBlocked(isBlocked);
                userService.updateUser(user);
                return new ResponseContext(new HashMap<>(), new HashMap<>());
            } catch (ServiceException e) {
                log.error("Failed to update user");
            }
        }
        return new ResponseContext(Map.of(RequestConstant.VIOLATION_MESSAGE, violationsMessages), new HashMap<>());
    }
}
