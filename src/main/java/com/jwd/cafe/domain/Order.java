package com.jwd.cafe.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder(setterPrefix = "with")
public class Order extends AbstractEntity {
    private Long id;
    private Double cost;
    private String address;
    private String reviewCode;
    private LocalDateTime createDate;
    private LocalDateTime deliveryDate;
    private OrderStatus status;
    private PaymentMethod method;
    private User user;
    private Map<Product, Integer> products;
}