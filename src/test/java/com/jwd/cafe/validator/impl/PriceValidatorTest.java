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

class PriceValidatorTest {
    private PriceValidator priceValidator;

    @BeforeEach
    void setUp() {
        priceValidator = new PriceValidator(null);
    }

    @Test
    public void validateShouldReturnEmptyViolations() {
        Map<String, String> params = Map.of(RequestConstant.PRICE, "2.55");
        RequestContext requestContext = RequestContext.builder().withRequestParameters(params)
                .withLocale("en_US").build();

        assertThat(priceValidator.validate(requestContext)).isEmpty();
    }

    @Test
    public void validateShouldReturnViolationWhenPriceIsNull() {
        RequestContext requestContext = RequestContext.builder().withRequestParameters(new HashMap<>())
                .withLocale("en_US").build();

        assertThat(priceValidator.validate(requestContext)).isNotEmpty();
    }

    @Test
    public void validateShouldReturnViolationWhenPriceDoesntMatchPattern() {
        Map<String, String> params = Map.of(RequestConstant.PRICE, ".0s1");
        RequestContext requestContext = RequestContext.builder().withRequestParameters(params)
                .withLocale("en_US").build();
        assertThat(priceValidator.validate(requestContext)).isNotEmpty();
    }

    @AfterEach
    void tearDown() {
        priceValidator = null;
    }
}