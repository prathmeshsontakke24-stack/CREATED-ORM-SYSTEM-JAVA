package com.customorm.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Creates EntityManager instances
 */
public class EntityManagerFactory {
    private Configuration configuration;

    public EntityManagerFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public EntityManager createEntityManager() {
        try {
            Connection connection = DriverManager.getConnection(
                    configuration.getUrl(),
                    configuration.getUsername(),
                    configuration.getPassword()
            );
            return new EntityManager(connection, configuration);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create EntityManager", e);
        }
    }
}