package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.Command;
import com.jwd.cafe.command.CommandType;
import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.command.ResponseContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.Role;
import com.jwd.cafe.domain.User;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.UserService;
import com.jwd.cafe.util.LocalizationHelper;
import com.jwd.cafe.validator.impl.*;
import com.lambdaworks.crypto.SCryptUtil;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Log4j2
public class RestRegistrationCommand implements Command {
    public final UserService userService;

    public RestRegistrationCommand() {
        userService = UserService.getInstance();
    }

    @Override
    public ResponseContext execute(RequestContext requestContext) {
        Set<String> violationMessages =
                new NameValidator(new PasswordValidator(
                        new PasswordRepeatValidator(new EmailValidator(
                                new PhoneNumberValidator(null))))).validate(requestContext);

        if (violationMessages.isEmpty()) {
            String username = requestContext.getRequestParameters().get(RequestConstant.USERNAME);
            String email = requestContext.getRequestParameters().get(RequestConstant.EMAIL);
            String password = requestContext.getRequestParameters().get(RequestConstant.PASSWORD);
            String firstName = requestContext.getRequestParameters().get(RequestConstant.FIRST_NAME);
            String lastName = requestContext.getRequestParameters().get(RequestConstant.LAST_NAME);
            String number = requestContext.getRequestParameters().get(RequestConstant.PHONE_NUMBER);
            try {
                User user = User.builder()
                        .withRole(Role.USER).withUsername(username)
                        .withPassword(SCryptUtil.scrypt(password, 16, 16, 16))
                        .withEmail(email).withFirstName(firstName)
                        .withLastName(lastName).withBalance(0d)
                        .withLoyaltyPoints(0).withIsBlocked(false)
                        .withPhoneNumber(number).withIsActive(false)
                        .build();
                Optional<String> serverMessage = userService.register(user);
                if (serverMessage.isEmpty()) {
                    return new ResponseContext(
                            Map.of(RequestConstant.REDIRECT_COMMAND, CommandType.TO_LOGIN.getName(),
                                    RequestConstant.VERIFICATION_MESSAGE, LocalizationHelper.localize(
                                            requestContext.getLocale(), "verificationMessage.verifyPlease")),
                            new HashMap<>());
                } else {
                    return new ResponseContext(
                            Map.of(RequestConstant.SERVER_MESSAGE, LocalizationHelper.localize(
                                    requestContext.getLocale(), serverMessage.get())), new HashMap<>()

                    );
                }
            } catch (ServiceException e) {
                log.error("Registration failed by UserService", e);
                return new RestErrorCommand().execute(requestContext);
            }
        } else {
            return new ResponseContext(
                    Map.of(RequestConstant.VIOLATION_MESSAGE, violationMessages), new HashMap<>()
            );
        }
    }
}
