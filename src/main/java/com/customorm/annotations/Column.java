package com.customorm.annotations;

import java.lang.annotation.*;

/**
 * Maps a field to a database column
 * Usage: @Column(name = "user_name", nullable = false)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name();              // Column name in database
    boolean nullable() default true;
    int length() default 255;
    boolean unique() default false;
}