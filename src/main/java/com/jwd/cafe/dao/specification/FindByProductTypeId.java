package com.jwd.cafe.dao.specification;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FindByProductTypeId implements Specification {
    private final Integer id;

    public FindByProductTypeId(Integer id) {
        this.id = id;
    }

    @Override
    public String specify() {
        return " WHERE type_id = ?";
    }

    @Override
    public void prepareSpecification(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, id);
    }
}
