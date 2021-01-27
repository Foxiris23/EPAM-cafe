package com.jwd.cafe.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder(setterPrefix = "with")
public class Review extends AbstractEntity{
    private Long id;
    private String feedback;
    private Integer rate;
    private Order order;
}
