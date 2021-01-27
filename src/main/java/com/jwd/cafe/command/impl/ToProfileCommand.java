package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.PageConstant;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.User;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.UserService;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2
public class ToProfileCommand implements Command{
    private final UserService userService;

    public ToProfileCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        try {
            Optional<User> user = userService.findById(
                    ((User) requestContext.getSessionAttributes().get(RequestConstant.USER)).getId());
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
