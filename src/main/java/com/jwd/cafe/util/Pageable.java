package com.jwd.cafe.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class Pageable<T> {
    private final List<T> list;
    private final Integer page;
    private final Integer totalPages;

    public Pageable(List<T> list, Integer page, Long count, Integer perPage) {
        this.list = list;
        this.page = page;
        this.totalPages = (int) Math.ceil((double) count / perPage);;
    }
}