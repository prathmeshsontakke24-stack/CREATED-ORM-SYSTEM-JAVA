package com.customorm.annotations;

import java.lang.annotation.*;

/**
 * Specifies the table name for the entity
 * Usage: @Table(name = "users")
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    String name();  // Table name in database
}