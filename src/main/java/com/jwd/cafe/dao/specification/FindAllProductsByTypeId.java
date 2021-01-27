package com.jwd.cafe.dao.specification;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FindAllProductsByTypeId implements Specification {
    private final Integer id;
    private final Integer limit;
    private final Long offset;

    public FindAllProductsByTypeId(Integer id, Integer limit, Long offset) {
        this.id = id;
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public String specify() {
        return " WHERE type_id = ? LIMIT ?, ?";
    }

    @Override
    public void prepareSpecification(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, id);
        preparedStatement.setLong(2, offset);
        preparedStatement.setInt(3, limit);
    }
}
