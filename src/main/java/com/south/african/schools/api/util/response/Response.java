package com.south.african.schools.api.util.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Standardized api response structure.
 * @param <T> saf
 */
@RequiredArgsConstructor
@Getter
@SuppressWarnings({"checkstyle:javadocvariable"})
public class Response<T> {

    private final String requestId;
    private final T data;
    private final String nextToken;
}
