package com.jwd.cafe.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The representation of review
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder(setterPrefix = "with")
public class Review extends AbstractEntity{
    private Long id;
    private String feedback;
    private Integer rate;
    private Order order;
}
