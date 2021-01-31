package com.jwd.cafe.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The representation of product type
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder(setterPrefix = "with")
public class ProductType extends AbstractEntity {
    private Integer id;
    private String name;
    private String filename;
}