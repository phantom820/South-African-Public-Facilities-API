package com.south.african.schools.api.util.filter;

/**
 * Custom exception for filter util mainly when we use reflection and may run into illegal access exception. This
 * extendsa runtime exception so that we can easily throw it in lambdas.
 */
public class FilterUtilException extends RuntimeException {
}
