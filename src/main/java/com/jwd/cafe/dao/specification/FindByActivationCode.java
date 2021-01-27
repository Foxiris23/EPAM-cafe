package com.jwd.cafe.dao.specification;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FindByActivationCode implements Specification {
    private final String code;

    public FindByActivationCode(String code) {
        this.code = code;
    }

    @Override
    public String specify() {
        return " WHERE activation_code = ?";
    }

    @Override
    public void prepareSpecification(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, code);
    }
}
