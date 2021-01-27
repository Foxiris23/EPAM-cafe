package com.jwd.cafe.dao.specification;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FindAllOrdersSortByStatus implements Specification {
    private final Integer limit;
    private final Long offset;

    public FindAllOrdersSortByStatus(Integer limit, Long offset) {
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public String specify() {
        return " ORDER BY status_id LIMIT ?, ?";
    }

    @Override
    public void prepareSpecification(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, offset);
        preparedStatement.setInt(2, limit);
    }
}
