package com.jwd.cafe.command;

import com.jwd.cafe.command.impl.RestErrorCommand;
import com.jwd.cafe.command.impl.RestLoginCommand;
import com.jwd.cafe.constant.RequestConstant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RestCommandFactoryTest {
    private RestCommandFactory restCommandFactory;

    @BeforeEach
    public void setUp() {
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

    @AfterEach
    public void tearDown() {
        restCommandFactory = null;
    }
}