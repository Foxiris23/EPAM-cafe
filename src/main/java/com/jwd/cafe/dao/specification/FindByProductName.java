package com.jwd.cafe.dao.specification;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FindByProductName implements Specification {
    private final String name;

    public FindByProductName(String name) {
        this.name = name;
    }

    @Override
    public String specify() {
        return " WHERE product_name = ?";
    }

    @Override
    public void prepareSpecification(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, name);
    }
}
