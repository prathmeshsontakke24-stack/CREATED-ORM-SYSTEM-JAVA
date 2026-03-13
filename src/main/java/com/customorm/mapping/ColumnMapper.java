package com.customorm.mapping;

import com.customorm.annotations.Column;
import com.customorm.annotations.Id;
import com.customorm.annotations.Ignore;

import java.lang.reflect.Field;

/**
 * Maps Java fields to database columns
 */
public class ColumnMapper {
    private Field field;
    private String columnName;
    private boolean isId;
    private boolean autoGenerate;
    private boolean nullable;
    private int length;
    private boolean unique;

    public ColumnMapper(Field field) {
        this.field = field;
        this.field.setAccessible(true); // Allow access to private fields
        mapColumn();
    }

    private void mapColumn() {
        // Check if field is ID
        if (field.isAnnotationPresent(Id.class)) {
            this.isId = true;
            Id id = field.getAnnotation(Id.class);
            this.autoGenerate = id.autoGenerate();
        }

        // Map column annotation
        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            this.columnName = column.name();
            this.nullable = column.nullable();
            this.length = column.length();
            this.unique = column.unique();
        } else {
            // Use field name as column name if no annotation
            this.columnName = field.getName();
        }
    }

    public boolean shouldIgnore() {
        return field.isAnnotationPresent(Ignore.class);
    }

    // Getters
    public Field getField() { return field; }
    public String getColumnName() { return columnName; }
    public boolean isId() { return isId; }
    public boolean isAutoGenerate() { return autoGenerate; }
    public boolean isNullable() { return nullable; }
    public int getLength() { return length; }
    public boolean isUnique() { return unique; }
}