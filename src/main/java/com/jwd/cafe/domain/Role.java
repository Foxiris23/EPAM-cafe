package com.jwd.cafe.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Role extends AbstractEntity{
    public static final Role USER = new Role(1, "ADMIN");
    public static final Role ADMIN = new Role(2, "USER");

    private final Integer id;
    private final String name;
}