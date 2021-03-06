package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.constant.RequestConstant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {
    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator(null);
    }

    @Test
    public void validateShouldReturnEmptyViolations() {
        Map<String, String> params = Map.of(RequestConstant.PASSWORD, "Mm12345");
        RequestContext requestContext = RequestContext.builder().withRequestParameters(params)
                .withLocale("en_US").build();

        assertThat(passwordValidator.validate(requestContext)).isEmpty();
    }

    @Test
    public void validateShouldReturnViolationWhenPasswordIsNull() {
        RequestContext requestContext = RequestContext.builder().withRequestParameters(new HashMap<>())
                .withLocale("en_US").build();

        assertThat(passwordValidator.validate(requestContext)).isNotEmpty();
    }

    @Test
    public void validateShouldReturnViolationWhenPasswordDoesntMatchPattern() {
        Map<String, String> params = Map.of(RequestConstant.PASSWORD, "mm12345");
        RequestContext requestContext = RequestContext.builder().withRequestParameters(params)
                .withLocale("en_US").build();

        assertThat(passwordValidator.validate(requestContext)).isNotEmpty();
    }

    @AfterEach
    void tearDown() {
        passwordValidator = null;
    }
}