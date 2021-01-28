package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.constant.RequestConstant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UsernameValidatorTest {
    private UsernameValidator usernameValidator;

    @BeforeEach
    public void setUp() {
        usernameValidator = new UsernameValidator(null);
    }

    @Test
    public void validateShouldReturnEmptyViolations() {
        Map<String, String> params = Map.of(RequestConstant.USERNAME, "Testing");
        RequestContext requestContext = RequestContext.builder().withRequestParameters(params)
                .withLocale("en_US").build();

        assertThat(usernameValidator.validate(requestContext)).isEmpty();
    }

    @Test
    public void validateShouldReturnViolationWhenUsernameIsNull() {
        RequestContext requestContext = RequestContext.builder().withRequestParameters(new HashMap<>())
                .withLocale("en_US").build();

        assertThat(usernameValidator.validate(requestContext)).isNotEmpty();
    }

    @Test
    public void validateShouldReturnViolationWhenUsernameDoesntMatchPattern() {
        Map<String, String> params = Map.of(RequestConstant.USERNAME, "Test1132@");
        RequestContext requestContext = RequestContext.builder().withRequestParameters(params)
                .withLocale("en_US").build();

        assertThat(usernameValidator.validate(requestContext)).isNotEmpty();
    }

    @AfterEach
    public void tearDown() {
        usernameValidator = null;
    }
}