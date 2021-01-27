package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.*;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.User;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.OrderService;
import com.jwd.cafe.service.ReviewService;
import com.jwd.cafe.service.UserService;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class RestTopUpCommand implements Command{
    public final UserService userService;

    public RestTopUpCommand() {
        userService = UserService.getInstance();
    }

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        User user = (User) requestContext.getSessionAttributes().get(RequestConstant.USER);
        user.setBalance(user.getBalance() + 30);
        try {
            userService.updateUser(user);
            return new ResponseContext(Map.of(RequestConstant.REDIRECT_COMMAND, CommandType.TO_PROFILE.getName()),
                    new HashMap<>());
        } catch (ServiceException e) {
            log.error("Failed to top up balance of user with id:" + user.getId());
        }
        return RestCommandType.ERROR.getCommand().execute(requestContext);
    }
}
