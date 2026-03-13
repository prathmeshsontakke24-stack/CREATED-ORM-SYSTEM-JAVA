package com.customorm.query;

import com.customorm.annotations.Column;
import com.customorm.annotations.Table;
import com.customorm.core.EntityManager;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Query<T> {

    private Connection connection;
    private Class<T> entityClass;
    private Criteria criteria;

    public Query(EntityManager entityManager, Class<T> entityClass) {
        this.connection = entityManager.getConnection();
        this.entityClass = entityClass;
    }

    public Query<T> where(Criteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public List<T> list() {

        List<T> results = new ArrayList<>();

        try {

            Table table = entityClass.getAnnotation(Table.class);
            String tableName = table.name();

            StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName);

            List<Object> parameters = new ArrayList<>();

            if (criteria != null) {
                sql.append(" WHERE ");
                sql.append(criteria.toSql(parameters));
            }

            System.out.println("Executing query: " + sql);

            PreparedStatement stmt = connection.prepareStatement(sql.toString());

            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                T entity = entityClass.getDeclaredConstructor().newInstance();

                for (Field field : entityClass.getDeclaredFields()) {

                    field.setAccessible(true);

                    // Convert Java field name to DB column name
                    String columnName = camelToSnake(field.getName());

                    Column column = field.getAnnotation(Column.class);

                    if(column == null) continue;

                    columnName = column.name();

                    Object value = rs.getObject(columnName);

                    // Handle numeric type conversion
                    if (value != null) {

                        Class<?> fieldType = field.getType();

                        if (fieldType == Long.class || fieldType == long.class) {
                            value = ((Number) value).longValue();
                        } else if (fieldType == Integer.class || fieldType == int.class) {
                            value = ((Number) value).intValue();
                        } else if (fieldType == Double.class || fieldType == double.class) {
                            value = ((Number) value).doubleValue();
                        } else if (fieldType == Float.class || fieldType == float.class) {
                            value = ((Number) value).floatValue();
                        }
                    }

                    field.set(entity, value);
                }

                results.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public Query<T> orderBy(String name) {
        return this;
    }

    // Convert camelCase to snake_case
    private String camelToSnake(String name) {

        StringBuilder result = new StringBuilder();

        for (char c : name.toCharArray()) {

            if (Character.isUpperCase(c)) {
                result.append("_").append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }
}