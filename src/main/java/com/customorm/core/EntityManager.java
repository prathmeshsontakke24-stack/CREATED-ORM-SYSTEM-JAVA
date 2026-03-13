package com.customorm.core;

import com.customorm.exception.EntityNotFoundException;
import com.customorm.exception.ORMException;
import com.customorm.mapping.ColumnMapper;
import com.customorm.mapping.EntityMapper;
import com.customorm.query.Query;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class for database operations
 */
public class EntityManager {

    private Connection connection;
    private Configuration configuration;
    private boolean showSql;

    public EntityManager(Connection connection, Configuration configuration) {
        this.connection = connection;
        this.configuration = configuration;
        this.showSql = configuration.isShowSql();
    }

    // ===============================
    // INSERT / PERSIST
    // ===============================
    public <T> void persist(T entity) {

        try {

            @SuppressWarnings("unchecked")
            Class<T> entityClass = (Class<T>) entity.getClass();
            EntityMapper<T> mapper = new EntityMapper<>(entityClass);

            String sql = QueryBuilder.buildInsertQuery(mapper, entity);

            if (showSql) {
                System.out.println("Executing: " + sql);
            }

            try (PreparedStatement stmt =
                         connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                int paramIndex = 1;

                for (ColumnMapper column : mapper.getColumns()) {

                    if (!column.isId() || !column.isAutoGenerate()) {

                        Object value = column.getField().get(entity);

                        stmt.setObject(paramIndex++, value);
                    }
                }

                stmt.executeUpdate();

                // ===============================
                // HANDLE AUTO GENERATED ID
                // ===============================
                ResultSet generatedKeys = stmt.getGeneratedKeys();

                if (generatedKeys.next() && mapper.getIdColumn() != null) {

                    Object generatedId = generatedKeys.getObject(1);

                    var idField = mapper.getIdColumn().getField();

                    idField.setAccessible(true);

                    if (generatedId instanceof Number) {
                        idField.set(entity, ((Number) generatedId).longValue());
                    } else {
                        idField.set(entity, generatedId);
                    }
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new ORMException(
                    "Duplicate value detected. This record already exists (probably email must be unique).",
                    e
            );
        }
        catch (Exception e) {
            throw new ORMException("Error persisting entity", e);
        }

    }

    // ===============================
    // FIND BY ID
    // ===============================
    public <T> T find(Class<T> entityClass, Object id) {

        try {

            EntityMapper<T> mapper = new EntityMapper<>(entityClass);

            String sql = QueryBuilder.buildSelectByIdQuery(mapper);

            if (showSql) {
                System.out.println("Executing: " + sql + " with id: " + id);
            }

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {

                stmt.setObject(1, id);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {

                    T entity = entityClass.getDeclaredConstructor().newInstance();

                    mapResultSetToEntity(rs, entity, mapper);

                    return entity;
                }

                throw new EntityNotFoundException(
                        "Entity " + entityClass.getSimpleName()
                                + " with id " + id + " not found");

            }

        } catch (Exception e) {

            throw new ORMException("Error finding entity", e);

        }
    }

    // ===============================
    // FIND ALL
    // ===============================
    public <T> List<T> findAll(Class<T> entityClass) {

        List<T> results = new ArrayList<>();

        try {

            EntityMapper<T> mapper = new EntityMapper<>(entityClass);

            String sql = QueryBuilder.buildSelectAllQuery(mapper);

            if (showSql) {
                System.out.println("Executing: " + sql);
            }

            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    T entity = entityClass.getDeclaredConstructor().newInstance();

                    mapResultSetToEntity(rs, entity, mapper);

                    results.add(entity);
                }
            }

        } catch (Exception e) {

            throw new ORMException("Error finding all entities", e);

        }

        return results;
    }

    // ===============================
    // UPDATE
    // ===============================
    public <T> void update(T entity) {

        try {

            @SuppressWarnings("unchecked")
            Class<T> entityClass = (Class<T>) entity.getClass();

            EntityMapper<T> mapper = new EntityMapper<>(entityClass);

            String sql = QueryBuilder.buildUpdateQuery(mapper);

            if (showSql) {
                System.out.println("Executing: " + sql);
            }

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {

                int paramIndex = 1;

                for (ColumnMapper column : mapper.getColumns()) {

                    if (!column.isId()) {

                        Object value = column.getField().get(entity);

                        stmt.setObject(paramIndex++, value);
                    }
                }

                Object idValue = mapper.getIdColumn().getField().get(entity);

                stmt.setObject(paramIndex, idValue);

                stmt.executeUpdate();
            }

        } catch (Exception e) {

            throw new ORMException("Error updating entity", e);

        }
    }

    // ===============================
    // DELETE
    // ===============================
    public <T> void delete(T entity) {

        try {

            @SuppressWarnings("unchecked")
            Class<T> entityClass = (Class<T>) entity.getClass();

            EntityMapper<T> mapper = new EntityMapper<>(entityClass);

            String sql = QueryBuilder.buildDeleteQuery(mapper);

            if (showSql) {
                System.out.println("Executing: " + sql);
            }

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {

                Object idValue = mapper.getIdColumn().getField().get(entity);

                stmt.setObject(1, idValue);

                stmt.executeUpdate();
            }

        } catch (Exception e) {

            throw new ORMException("Error deleting entity", e);

        }
    }

    // ===============================
    // CREATE QUERY
    // ===============================
    public <T> Query<T> createQuery(Class<T> entityClass) {

        return new Query<>(this, entityClass);

    }

    // ===============================
    // MAP RESULTSET → ENTITY
    // ===============================
    public <T> void mapResultSetToEntity(ResultSet rs, T entity, EntityMapper<T> mapper)
            throws Exception {

        for (ColumnMapper column : mapper.getColumns()) {

            Object value = rs.getObject(column.getColumnName());

            var field = column.getField();
            field.setAccessible(true);

            if (value != null) {

                Class<?> fieldType = field.getType();

                if (fieldType == Long.class || fieldType == long.class) {
                    value = ((Number) value).longValue();
                }
                else if (fieldType == Integer.class || fieldType == int.class) {
                    value = ((Number) value).intValue();
                }
                else if (fieldType == Double.class || fieldType == double.class) {
                    value = ((Number) value).doubleValue();
                }
            }

            field.set(entity, value);
        }
    }

    // ===============================
    // GETTERS
    // ===============================
    public Connection getConnection() {
        return connection;
    }

    public boolean isShowSql() {
        return showSql;
    }

    // ===============================
    // CLOSE CONNECTION
    // ===============================
    public void close() {

        try {

            if (connection != null && !connection.isClosed()) {

                connection.close();
            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public <T> T mapResultSetToEntity(Class<T> entityClass, ResultSet rs) {
        return null;
    }
}