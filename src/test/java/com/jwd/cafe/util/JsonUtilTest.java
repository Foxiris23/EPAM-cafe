package com.jwd.cafe.util;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilTest {

    @Test
    public void jsonToCartTestShouldReturnCart() {
        String json = "{\"cart\":[{\"id\":\"1\",\"amount\":\"2\"}," +
                "{\"id\":\"2\",\"amount\":\"3\"},{\"id\":\"3\",\"amount\":\"1\"}]}";
        Map<Integer, Integer> cart = Map.of(1, 2, 2, 3, 3, 1);
        assertThat(JsonUtil.jsonToCart(json)).isEqualTo(cart);
    }
}