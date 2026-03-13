package com.customorm.query;

import java.util.ArrayList;
import java.util.List;

public class Criteria {

    public Criteria greaterThan(String age, int i) {
        return null;
    }

    private static class Condition {
        String field;
        Operator operator;
        Object value;

        Condition(String field, Operator operator, Object value) {
            this.field = field;
            this.operator = operator;
            this.value = value;
        }
    }

    private List<Condition> conditions = new ArrayList<>();

    public Criteria add(String field, Operator operator, Object value) {
        conditions.add(new Condition(field, operator, value));
        return this;
    }

    public String toSql(List<Object> parameters) {

        if (conditions.isEmpty()) {
            return "";
        }

        StringBuilder sql = new StringBuilder();

        for (int i = 0; i < conditions.size(); i++) {

            Condition c = conditions.get(i);

            if (i > 0) {
                sql.append(" AND ");
            }

            sql.append(c.field)
                    .append(" ")
                    .append(c.operator.getSymbol())
                    .append(" ?");

            parameters.add(c.value);
        }

        return sql.toString();
    }
}