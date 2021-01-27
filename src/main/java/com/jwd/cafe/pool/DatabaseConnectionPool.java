package com.jwd.cafe.pool;

import com.jwd.cafe.config.DatabaseConfig;
import com.jwd.cafe.exception.ApplicationStartException;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Log4j2
public class DatabaseConnectionPool {
    private static volatile DatabaseConnectionPool instance;
    private BlockingQueue<Connection> connectionPool;

    public static DatabaseConnectionPool getInstance() {
        DatabaseConnectionPool localInstance = instance;
        if (localInstance == null) {
            synchronized (DatabaseConnectionPool.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DatabaseConnectionPool();
                }
            }
        }
        return localInstance;
    }

    public void initConnectionPool() throws ClassNotFoundException {
        DatabaseConfig dbConfig = DatabaseConfig.getInstance();
        Integer initSize = dbConfig.getPoolSize();
        connectionPool = new ArrayBlockingQueue<>(initSize);
        try {
            for (int i = 0; i < initSize; i++) {
                connectionPool.offer(new DatabaseConnectionProxy(DriverManager.getConnection(
                        dbConfig.getDbUrl(), dbConfig.getUsername(), dbConfig.getPassword()))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationStartException("Failed to initialize database connection pool");
        }
    }

    public void destroyConnectionPool() throws SQLException {
        try {
            for (Connection connection : connectionPool) {
                connection.close();
            }
        } catch (SQLException e) {
            log.error("Failed to close a connection");
        }
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            DriverManager.deregisterDriver(drivers.nextElement());
        }
    }

    public Connection getConnection() {
        try {
            return connectionPool.take();
        } catch (InterruptedException e) {
            log.error("Waiting for a connection was interrupted");
            throw new RuntimeException();
        }
    }

    public void releaseConnection(final Connection connection) throws SQLException {
        if (connection instanceof DatabaseConnectionProxy) {
            connection.setAutoCommit(true);
            connectionPool.offer(connection);
        } else {
            log.warn("Attempt to return an unproxied connection");
        }
    }

    private DatabaseConnectionPool() {
    }
}
