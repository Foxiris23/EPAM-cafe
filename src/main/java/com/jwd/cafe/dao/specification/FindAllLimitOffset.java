package com.jwd.cafe.dao.specification;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FindAllLimitOffset implements Specification {
    private final Integer limit;
    private final Long offset;

    public FindAllLimitOffset(Integer limit, Long offset) {
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public String specify() {
        return " limit ?, ?";
    }

    @Override
    public void prepareSpecification(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, offset);
        preparedStatement.setInt(2, limit);
    }
}
