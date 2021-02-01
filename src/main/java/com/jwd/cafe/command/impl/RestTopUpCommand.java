package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.User;
import com.jwd.cafe.domain.dto.UserDto;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.UserService;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Top up {@link User}'s balance
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class RestTopUpCommand implements Command {
    private final UserService userService;

    public RestTopUpCommand(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     * executes {@link RestErrorCommand} if {@link ServiceException} was caught
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        UserDto userDto = (UserDto) requestContext.getSessionAttributes().get(RequestConstant.USER);
        try {
            Optional<User> userOptional = userService.findById(userDto.getId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setBalance(user.getBalance() + 30);
                userService.updateUser(user);
                return new ResponseContext(Map.of(RequestConstant.REDIRECT_COMMAND, CommandType.USER_TO_PROFILE.getName()),
                        new HashMap<>());
            }
        } catch (ServiceException e) {
            log.error("Failed to top up balance of user with id:" + userDto.getId());
        }
        return RestCommandType.ERROR.getCommand().execute(requestContext);
    }
}
