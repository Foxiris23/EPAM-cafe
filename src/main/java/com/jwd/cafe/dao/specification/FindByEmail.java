package com.jwd.cafe.dao.specification;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FindByEmail implements Specification {
    private final String email;

    public FindByEmail(String email) {
        this.email = email;
    }

    @Override
    public String specify() {
        return " WHERE email = ?";
    }

    @Override
    public void prepareSpecification(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, email);
    }
}
