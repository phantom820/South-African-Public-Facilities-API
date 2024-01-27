package com.south.african.data.api.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String nextToken;
}
