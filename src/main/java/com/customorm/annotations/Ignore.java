package com.customorm.annotations;

import java.lang.annotation.*;

/**
 * Fields marked with this annotation will be ignored by ORM
 * Usage: @Ignore
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Ignore {
}