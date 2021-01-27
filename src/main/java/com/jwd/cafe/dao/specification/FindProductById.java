package com.jwd.cafe.dao.specification;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FindProductById implements Specification {
    private final Integer id;

    public FindProductById(Integer id) {
        this.id = id;
    }

    @Override
    public String specify() {
        return " WHERE id = ?";
    }

    @Override
    public void prepareSpecification(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, id);
    }
}
