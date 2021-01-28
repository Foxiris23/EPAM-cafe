package com.jwd.cafe.validator.impl;

import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.constant.RequestConstant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ProductNameValidatorTest {
    private ProductNameValidator productNameValidator;

    @BeforeEach
    void setUp() {
        productNameValidator = new ProductNameValidator(null);
    }

    @Test
    public void validateShouldReturnEmptyViolations() {
        Map<String, String> params = Map.of(RequestConstant.PRODUCT_NAME, "Carlo's burgers");
        RequestContext requestContext = RequestContext.builder().withRequestParameters(params)
                .withLocale("en_US").build();

        assertThat(productNameValidator.validate(requestContext)).isEmpty();
    }

    @Test
    public void validateShouldReturnViolationWhenNameIsNull() {
        RequestContext requestContext = RequestContext.builder().withRequestParameters(new HashMap<>())
                .withLocale("en_US").build();

        assertThat(productNameValidator.validate(requestContext)).isNotEmpty();
    }

    @Test
    public void validateShouldReturnViolationWhenNameDoesntMatchPattern() {
        Map<String, String> params = Map.of(RequestConstant.PRODUCT_NAME, "Carlo's$ burgers");
        RequestContext requestContext = RequestContext.builder().withRequestParameters(params)
                .withLocale("en_US").build();

        assertThat(productNameValidator.validate(requestContext)).isNotEmpty();
    }

    @AfterEach
    void tearDown() {
        productNameValidator = null;
    }
}