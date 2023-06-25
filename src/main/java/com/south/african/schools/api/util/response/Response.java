package com.south.african.schools.api.util.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Standardized api response structure.
 * @param <T>
 */
@RequiredArgsConstructor
@Getter
@SuppressWarnings({"checkstyle:javadocvariable"})
public final class Response<T extends List<?>> {

    private final String requestId;
    private final T data;
    private final String nextToken;
}
