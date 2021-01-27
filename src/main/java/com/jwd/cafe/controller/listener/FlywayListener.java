package com.jwd.cafe.controller.listener;

import com.jwd.cafe.config.FlywayConfig;
import org.flywaydb.core.Flyway;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

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