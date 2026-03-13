package com.customorm.annotations;

import java.lang.annotation.*;

/**
 * Marks a field as the primary key
 * Usage: @Id
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
    // Whether the ID is auto-generated
    boolean autoGenerate() default true;
}