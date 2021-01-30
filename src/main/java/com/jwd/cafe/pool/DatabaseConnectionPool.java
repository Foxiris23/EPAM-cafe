package com.jwd.cafe.pool;

import com.jwd.cafe.config.DatabaseConfig;
import com.jwd.cafe.exception.ApplicationStartException;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Enumeration;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Log4j2
public class DatabaseConnectionPool {
    private static volatile DatabaseConnectionPool instance;
    private final DatabaseConfig dbConfig;
    private BlockingQueue<Connection> availableConnections;
    private Queue<Connection> takenConnections;

    public static DatabaseConnectionPool getInstance() {
        DatabaseConnectionPool localInstance = instance;
        if (localInstance == null) {
            synchronized (DatabaseConnectionPool.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DatabaseConnectionPool(DatabaseConfig.getInstance());
                }
            }
        }
        return localInstance;
    }

    public void initConnectionPool() throws ClassNotFoundException {
        Integer initSize = dbConfig.getPoolSize();
        availableConnections = new ArrayBlockingQueue<>(initSize);
        takenConnections = new ArrayDeque<>();
        try {
            for (int i = 0; i < initSize; i++) {
                availableConnections.offer(new DatabaseConnectionProxy(DriverManager.getConnection(
                        dbConfig.getDbUrl(), dbConfig.getUsername(), dbConfig.getPassword()))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationStartException("Failed to initialize database connection pool");
        }
    }

    public void destroyConnectionPool(boolean withDeregistration) throws SQLException {
        try {
            for (Connection connection : availableConnections) {
                ((DatabaseConnectionProxy) connection).realClose();
            }

            for (Connection connection : takenConnections) {
                connection.commit();
                ((DatabaseConnectionProxy) connection).realClose();
            }

        } catch (SQLException e) {
            log.error("Failed to close a connection");
        }
        if (withDeregistration) {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                DriverManager.deregisterDriver(drivers.nextElement());
            }
        }
    }

    public Connection getConnection() {
        try {
            Connection connection = availableConnections.take();
            takenConnections.add(connection);
            return connection;
        } catch (InterruptedException e) {
            log.error("Waiting for a connection was interrupted");
            throw new RuntimeException();
        }
    }

    public void releaseConnection(final Connection connection) throws SQLException {
        if (connection instanceof DatabaseConnectionProxy) {
            connection.setAutoCommit(true);
            takenConnections.remove(connection);
            availableConnections.offer(connection);
        } else {
            log.warn("Attempt to return an unproxied connection");
        }
    }

    private DatabaseConnectionPool(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }
}
