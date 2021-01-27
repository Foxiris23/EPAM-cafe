package com.jwd.cafe.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

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