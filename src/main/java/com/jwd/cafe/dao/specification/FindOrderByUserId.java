package com.jwd.cafe.dao.specification;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FindOrderByUserId implements Specification {
    private final Long id;

    public FindOrderByUserId(Long id) {
        this.id = id;
    }


    @Override
    public String specify() {
        return " WHERE user_id = ?";
    }

    @Override
    public void prepareSpecification(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, id);
    }
}
