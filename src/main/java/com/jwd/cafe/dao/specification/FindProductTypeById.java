package com.jwd.cafe.dao.specification;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FindProductTypeById implements Specification {
    private static final String SQL_WHERE_ID = " WHERE id = ?";

    private final Integer id;

    public FindProductTypeById(Integer id) {
        this.id = id;
    }

    @Override
    public String specify() {
        return SQL_WHERE_ID;
    }

    @Override
    public void prepareSpecification(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, id);
    }
}
