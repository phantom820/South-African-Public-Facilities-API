package com.south.african.schools.api.util.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for implementing api filtering on entities.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Filter {

    /**
     * Default name value for annotation.
     */
    String DEFAULT_FILTER_NAME = "FILTER";

    /**
     * Custom name for filter, so that we use this for lookup in query params.
     * @return The name of the filter.
     */
    String name() default DEFAULT_FILTER_NAME;
}
