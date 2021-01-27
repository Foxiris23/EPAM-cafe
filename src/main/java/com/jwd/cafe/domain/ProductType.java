package com.jwd.cafe.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder(setterPrefix = "with")
public class ProductType extends AbstractEntity {
    private Integer id;
    private String name;
    private String filename;
}