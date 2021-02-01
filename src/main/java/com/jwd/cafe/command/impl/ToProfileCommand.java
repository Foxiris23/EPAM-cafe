package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.PageConstant;
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
 * Moves an user to his profile page
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class ToProfileCommand implements Command{
    private final UserService userService;

    public ToProfileCommand(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param requestContext contains all data received with {@link javax.servlet.http.HttpServletRequest}
     * @return instance of {@link ResponseContext}
     * executes {@link ErrorCommand} if {@link ServiceException} was caught
     */
    @Override
    public ResponseContext execute(RequestContext requestContext) {
        try {
            Optional<User> user = userService.findById(
                    ((UserDto) requestContext.getSessionAttributes().get(RequestConstant.USER)).getId());
            if (user.isPresent()) {
                return new ResponseContext(new ForwardResponse(ResponseType.Type.FORWARD, PageConstant.PROFILE),
                        Map.of(RequestConstant.USER, user.get()), new HashMap<>());
            }
        } catch (ServiceException e) {
            log.error("Failed to move to user's profile");
        }
        return CommandType.ERROR.getCommand().execute(requestContext);
    }
}
