package com.jwd.cafe.controller.listener;

import com.jwd.cafe.config.FlywayConfig;
import com.jwd.cafe.pool.DatabaseConnectionPool;
import org.flywaydb.core.Flyway;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Inits {@link Flyway} migrations on application start up
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@WebListener
public class FlywayListener implements ServletContextListener {
    private final FlywayConfig flywayConfig;

    public FlywayListener() {
        flywayConfig = FlywayConfig.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Flyway flyway = new Flyway(flywayConfig);
        flyway.migrate();
    }
}