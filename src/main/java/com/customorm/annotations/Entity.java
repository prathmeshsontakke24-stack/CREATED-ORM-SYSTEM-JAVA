package com.customorm.annotations;

import java.lang.annotation.*;

/**
 * Marks a class as an entity (maps to a database table)
 * Usage: @Entity
 */
@Retention(RetentionPolicy.RUNTIME)  // Available at runtime
@Target(ElementType.TYPE)            // Can be used on classes
public @interface Entity {
    // Optional name for the entity
    String name() default "";
}