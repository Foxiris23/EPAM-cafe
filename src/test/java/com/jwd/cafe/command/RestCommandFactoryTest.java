package com.jwd.cafe.command;

import com.jwd.cafe.command.impl.ErrorCommand;
import com.jwd.cafe.command.impl.RestErrorCommand;
import com.jwd.cafe.command.impl.RestLoginCommand;
import com.jwd.cafe.constant.RequestConstant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RestCommandFactoryTest {
    private RestCommandFactory restCommandFactory;

    @Before
    public void beforeTest() {
        restCommandFactory = RestCommandFactory.getInstance();
    }

    @Test
    public void getCommandShouldReturnRestLoginCommand() {
        RequestContext requestContext = RequestContext.builder()
                .withRequestParameters(Map.of(RequestConstant.COMMAND, RestCommandType.LOGIN.getName()))
                .build();

        assertThat(restCommandFactory.getCommand(requestContext)).isInstanceOf(RestLoginCommand.class);
    }

    @Test
    public void getCommandShouldReturnRestErrorCommandWhenCommandIsEmpty() {
        RequestContext requestContext = RequestContext.builder()
                .withRequestParameters(Map.of(RequestConstant.COMMAND, ""))
                .build();

        assertThat(restCommandFactory.getCommand(requestContext)).isInstanceOf(RestErrorCommand.class);
    }

    @Test
    public void getCommandShouldReturnErrorCommandWhenCommandIsNull() {
        RequestContext requestContext = RequestContext.builder()
                .withRequestParameters(new HashMap<>())
                .build();

        assertThat(restCommandFactory.getCommand(requestContext)).isInstanceOf(RestErrorCommand.class);
    }

    @After
    public void afterTest() {
        restCommandFactory = null;
    }
}