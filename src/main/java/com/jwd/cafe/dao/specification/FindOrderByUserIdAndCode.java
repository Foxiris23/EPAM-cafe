package com.jwd.cafe.dao.specification;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FindOrderByUserIdAndCode implements Specification {
    private final Long id;
    private final String code;

    public FindOrderByUserIdAndCode(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    @Override
    public String specify() {
        return " WHERE user_id = ? and review_code = ?";
    }

    @Override
    public void prepareSpecification(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, id);
        preparedStatement.setString(2, code);
    }
}
