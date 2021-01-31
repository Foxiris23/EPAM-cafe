package com.jwd.cafe.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * The representation of order
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
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