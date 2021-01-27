package com.jwd.cafe.dao.specification;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FindByTypeName implements Specification {
    private final String typeName;

    public FindByTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String specify() {
        return " WHERE type_name = ?";
    }

    @Override
    public void prepareSpecification(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, typeName);
    }
}
