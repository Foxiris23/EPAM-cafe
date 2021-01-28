package com.jwd.cafe.command.impl;

import com.jwd.cafe.command.CommandType;
import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.command.ResponseContext;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.User;
import com.jwd.cafe.exception.ServiceException;
import com.jwd.cafe.service.UserService;
import com.jwd.cafe.util.LocalizationHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestRegistrationCommandTest {
    @Mock
    private UserService userService;
    private RequestContext requestContext;
    private RestRegistrationCommand command;

    @BeforeEach
    public void setUp() {
        Map<String, String> params = new HashMap<>();
        params.put(RequestConstant.USERNAME, "test");
        params.put(RequestConstant.PASSWORD, "Test12345");
        params.put(RequestConstant.REPEAT_PASSWORD, "Test12345");
        params.put(RequestConstant.EMAIL, "hili.foxiris@gmail.com");
        params.put(RequestConstant.FIRST_NAME, "Test");
        params.put(RequestConstant.LAST_NAME, "Tester");
        params.put(RequestConstant.PHONE_NUMBER, "+375445960023");
        userService = mock(UserService.class);
        command = new RestRegistrationCommand(userService);
        requestContext = RequestContext.builder().withRequestParameters(params).withLocale("en_US").build();
    }

    @Test
    public void executeShouldReturnRespWithRedirectAndVerificationMessage() throws ServiceException {
        when(userService.register(any(User.class))).thenReturn(Optional.empty());
        ResponseContext responseContext = new ResponseContext(
                Map.of(RequestConstant.REDIRECT_COMMAND, CommandType.TO_LOGIN.getName(),
                        RequestConstant.VERIFICATION_MESSAGE, LocalizationHelper.localize(
                                requestContext.getLocale(), "verificationMessage.verifyPlease")),
                new HashMap<>());
        assertThat(command.execute(requestContext)).isEqualTo(responseContext);
    }

    @Test
    public void executeShouldReturnErrorResponse() throws ServiceException {
        given(userService.register(any(User.class))).willThrow(new ServiceException());
        ResponseContext responseContext = new ResponseContext
                (Map.of(RequestConstant.REDIRECT_COMMAND, CommandType.ERROR.getName()), new HashMap<>());
        assertThat(command.execute(requestContext)).isEqualTo(responseContext);
    }

    @Test
    public void executeShouldReturnRespWithServerMessage() throws ServiceException {
        given(userService.register(any(User.class))).willReturn(Optional.of("serverMessage.usernameAlreadyTaken"));
        ResponseContext responseContext = new ResponseContext(
                Map.of(RequestConstant.SERVER_MESSAGE, LocalizationHelper.localize(
                        requestContext.getLocale(), "serverMessage.usernameAlreadyTaken")), new HashMap<>()
        );
        assertThat(command.execute(requestContext)).isEqualTo(responseContext);
    }

    @AfterEach
    public void tearDown() {
        userService = null;
        requestContext = null;
        command = null;
    }
}