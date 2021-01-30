package com.jwd.cafe.controller.listener;

import com.jwd.cafe.pool.DatabaseConnectionPool;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@Log4j2
@WebListener
public class ConnectionPoolListener implements ServletContextListener {
    private final DatabaseConnectionPool databaseConnectionPool;

    public ConnectionPoolListener() {
        databaseConnectionPool = DatabaseConnectionPool.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            databaseConnectionPool.initConnectionPool();
        } catch (ClassNotFoundException e) {
            log.error("Failed to init connection pool", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            databaseConnectionPool.destroyConnectionPool(true);
        } catch (SQLException e) {
            log.error("Failed to destroy connection pool.", e);
        }
    }
}
