package com.jwd.cafe.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The representation of product
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder(setterPrefix = "with")
public class Product extends AbstractEntity {
    private Integer id;
    private String name;
    private Double price;
    private String description;
    private ProductType productType;
    private String imgFilename;
}