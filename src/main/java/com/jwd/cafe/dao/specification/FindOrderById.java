package com.jwd.cafe.dao.specification;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FindOrderById implements Specification {
    private final Long id;

    public FindOrderById(Long id) {
        this.id = id;
    }

    @Override
    public String specify() {
        return " WHERE id = ?";
    }

    @Override
    public void prepareSpecification(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, id);
    }
}
