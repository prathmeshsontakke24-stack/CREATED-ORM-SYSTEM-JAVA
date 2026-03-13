package com.customorm.mapping;

import com.customorm.annotations.Entity;
import com.customorm.annotations.Table;
import com.customorm.exception.ORMException;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps Java classes to database tables
 */
public class EntityMapper<T> {
    private Class<T> entityClass;
    private String tableName;
    private List<ColumnMapper> columns = new ArrayList<>();
    private ColumnMapper idColumn;

    public EntityMapper(Class<T> entityClass) {
        this.entityClass = entityClass;
        validateEntity();
        mapEntity();
    }

    private void validateEntity() {
        if (!entityClass.isAnnotationPresent(Entity.class)) {
            throw new ORMException("Class " + entityClass.getName() +
                    " is not annotated with @Entity");
        }
    }

    private void mapEntity() {
        // Get table name
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            this.tableName = table.name();
        } else {
            // Use class name as table name
            this.tableName = entityClass.getSimpleName().toLowerCase();
        }

        // Map columns
        for (java.lang.reflect.Field field : entityClass.getDeclaredFields()) {
            ColumnMapper columnMapper = new ColumnMapper(field);

            if (!columnMapper.shouldIgnore()) {
                columns.add(columnMapper);

                if (columnMapper.isId()) {
                    if (idColumn != null) {
                        throw new ORMException("Multiple @Id fields found in " +
                                entityClass.getName());
                    }
                    this.idColumn = columnMapper;
                }
            }
        }

        // Validate that entity has an ID
        if (idColumn == null) {
            throw new ORMException("No @Id field found in " + entityClass.getName());
        }
    }

    // Getters
    public String getTableName() { return tableName; }
    public List<ColumnMapper> getColumns() { return columns; }
    public ColumnMapper getIdColumn() { return idColumn; }
}