package com.jwd.cafe.config;

import com.jwd.cafe.util.PropertyReaderUtil;
import lombok.Getter;

import java.util.Properties;

/**
 * The class encapsulates in itself all the data coming from the database properties file
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Getter
public class DatabaseConfig {
    private static volatile DatabaseConfig instance;

    public static DatabaseConfig getInstance() {
        DatabaseConfig localInstance = instance;
        if (localInstance == null) {
            synchronized (DatabaseConfig.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DatabaseConfig();
                }
            }
        }
        return localInstance;
    }

    private static final String PROPERTIES_PATH = "database";
    private static final String DB_JDBC_URL = "db.jdbc.url";
    private static final String DB_HOST = "db.host";
    private static final String DB_PORT = "db.port";
    private static final String DB_NAME = "db.name";
    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_POOLSIZE = "db.poolSize";

    private String jdbcUrl;
    private String host;
    private String port;
    private String name;
    private String username;
    private String password;
    private Integer poolSize;

    public void init() {
        Properties properties = PropertyReaderUtil.readProperties("database");
        jdbcUrl = properties.getProperty(DB_JDBC_URL);
        host = properties.getProperty(DB_HOST);
        port = properties.getProperty(DB_PORT);
        name = properties.getProperty(DB_NAME);
        username = properties.getProperty(DB_USERNAME);
        password = properties.getProperty(DB_PASSWORD);
        poolSize = Integer.parseInt(properties.getProperty(DB_POOLSIZE));
    }

    public String getDbUrl() {
        return String.format("%s%s:%s/%s", jdbcUrl, host, port, name);
    }

    private DatabaseConfig() {
        init();
    }

    @Override
    public String toString() {
        return "DatabaseConfig{" +
                "jdbcUrl='" + jdbcUrl + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", poolSize=" + poolSize +
                '}';
    }
}
