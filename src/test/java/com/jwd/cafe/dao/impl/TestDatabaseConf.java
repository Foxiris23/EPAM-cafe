package com.jwd.cafe.dao.impl;

import com.jwd.cafe.config.FlywayConfig;
import com.jwd.cafe.pool.DatabaseConnectionPool;
import org.flywaydb.core.Flyway;

import java.sql.SQLException;

public class TestDatabaseConf {
    Flyway flyway;
    DatabaseConnectionPool pool;

    public void initDatabase() throws ClassNotFoundException {
        flyway = new Flyway(FlywayConfig.getInstance());
        pool= DatabaseConnectionPool.getInstance();
        pool.initConnectionPool();
        flyway.clean();
        flyway.migrate();
    }

    public void destroyDatabase() throws SQLException {
        pool.destroyConnectionPool(false);
        flyway.clean();
    }
}
