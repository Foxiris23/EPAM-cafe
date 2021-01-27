package com.jwd.cafe.dao.specification;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FindAll implements Specification {
    @Override
    public String specify() {
        return "";
    }

    @Override
    public void prepareSpecification(PreparedStatement preparedStatement) throws SQLException {
    }
}
