package com.jwd.cafe.command;

import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.dao.impl.OrderDao;
import com.jwd.cafe.service.OrderService;
import com.jwd.cafe.service.UserService;

/**
 * The factory extracts command from the client's {@link RequestContext} and
 * returns ${@link Command} from {@link RestCommandType} using the Factory pattern
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public class RestCommandFactory {
    private static volatile RestCommandFactory instance;

    public static RestCommandFactory getInstance() {
        RestCommandFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (RestCommandFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new RestCommandFactory();
                }
            }
        }
        return localInstance;
    }

    private RestCommandFactory() {
    }

    public Command getCommand(RequestContext requestContext) {
        String commandName = requestContext.getRequestParameters().get(RequestConstant.COMMAND);
        return RestCommandType.getCommandByName(commandName);
    }
}