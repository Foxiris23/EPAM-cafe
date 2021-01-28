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

class ReviewCodeValidatorTest {
    private ReviewCodeValidator reviewCodeValidator;

    @BeforeEach
    void setUp() {
        reviewCodeValidator = new ReviewCodeValidator(null);
    }

    @Test
    public void validateShouldReturnEmptyViolations() {
        Map<String, String> params = Map.of(RequestConstant.CODE, "D2AJk1jdsl");
        RequestContext requestContext = RequestContext.builder().withRequestParameters(params)
                .withLocale("en_US").build();

        assertThat(reviewCodeValidator.validate(requestContext)).isEmpty();
    }

    @Test
    public void validateShouldReturnViolationWhenCodeIsNull() {
        RequestContext requestContext = RequestContext.builder().withRequestParameters(new HashMap<>())
                .withLocale("en_US").build();

        assertThat(reviewCodeValidator.validate(requestContext)).isNotEmpty();
    }

    @Test
    public void validateShouldReturnViolationWhenCodeDoesntMatchPattern() {
        Map<String, String> params = Map.of(RequestConstant.CODE, "D2AJk1jd@l");
        RequestContext requestContext = RequestContext.builder().withRequestParameters(params)
                .withLocale("en_US").build();

        assertThat(reviewCodeValidator.validate(requestContext)).isNotEmpty();
    }

    @AfterEach
    void tearDown() {

    }
}