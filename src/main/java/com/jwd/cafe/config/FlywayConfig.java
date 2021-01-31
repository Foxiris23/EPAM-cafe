package com.jwd.cafe.config;

import org.flywaydb.core.api.configuration.ClassicConfiguration;

/**
 * The class configures a {@link org.flywaydb.core.Flyway}
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public class FlywayConfig extends ClassicConfiguration {
    private static volatile FlywayConfig instance;

    public static FlywayConfig getInstance() {
        FlywayConfig localInstance = instance;
        if (localInstance == null) {
            synchronized (FlywayConfig.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new FlywayConfig();
                }
            }
        }
        return localInstance;
    }

    private void init() {
        DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
        this.setDataSource(
                databaseConfig.getDbUrl(),
                databaseConfig.getUsername(),
                databaseConfig.getPassword()
        );
    }

    private FlywayConfig() {
        init();
    }
}