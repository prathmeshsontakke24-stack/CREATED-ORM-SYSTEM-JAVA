package com.customorm.core;

import com.customorm.mapping.ColumnMapper;
import com.customorm.mapping.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds SQL queries dynamically
 */
public class QueryBuilder {

    // INSERT QUERY
    public static <T> String buildInsertQuery(EntityMapper<T> mapper, T entity) {

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ").append(mapper.getTableName()).append(" (");

        // Filter columns (exclude auto-generated ID)
        List<ColumnMapper> columns = mapper.getColumns().stream()
                .filter(col -> !col.isId() || !col.isAutoGenerate())
                .collect(Collectors.toList());

        // Column names
        String columnNames = columns.stream()
                .map(ColumnMapper::getColumnName)
                .collect(Collectors.joining(", "));

        query.append(columnNames).append(") VALUES (");

        // Placeholders
        String placeholders = columns.stream()
                .map(col -> "?")
                .collect(Collectors.joining(", "));

        query.append(placeholders).append(")");

        return query.toString();
    }

    // SELECT BY ID
    public static <T> String buildSelectByIdQuery(EntityMapper<T> mapper) {
        return "SELECT * FROM " + mapper.getTableName() +
                " WHERE " + mapper.getIdColumn().getColumnName() + " = ?";
    }

    // SELECT ALL
    public static <T> String buildSelectAllQuery(EntityMapper<T> mapper) {
        return "SELECT * FROM " + mapper.getTableName();
    }

    // UPDATE
    public static <T> String buildUpdateQuery(EntityMapper<T> mapper) {

        StringBuilder query = new StringBuilder();
        query.append("UPDATE ").append(mapper.getTableName()).append(" SET ");

        String setClause = mapper.getColumns().stream()
                .filter(col -> !col.isId())
                .map(col -> col.getColumnName() + " = ?")
                .collect(Collectors.joining(", "));

        query.append(setClause)
                .append(" WHERE ")
                .append(mapper.getIdColumn().getColumnName())
                .append(" = ?");

        return query.toString();
    }

    // DELETE
    public static <T> String buildDeleteQuery(EntityMapper<T> mapper) {
        return "DELETE FROM " + mapper.getTableName() +
                " WHERE " + mapper.getIdColumn().getColumnName() + " = ?";
    }
}