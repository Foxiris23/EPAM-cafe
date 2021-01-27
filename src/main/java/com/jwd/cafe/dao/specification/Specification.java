package com.jwd.cafe.dao.specification;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Specification {
    String specify();

    void prepareSpecification(PreparedStatement preparedStatement) throws SQLException;
}