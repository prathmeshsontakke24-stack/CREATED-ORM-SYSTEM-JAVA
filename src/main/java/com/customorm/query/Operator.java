package com.customorm.query;

/**
 * SQL Operators used in Criteria queries
 */
public enum Operator {

    EQ("="),      // equal
    GT(">"),      // greater than
    LT("<"),      // less than
    GTE(">="),    // greater than or equal
    LTE("<="),    // less than or equal
    NE("!=");     // not equal

    private final String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}