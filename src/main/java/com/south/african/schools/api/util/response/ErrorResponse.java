package com.south.african.schools.api.util.response;


import lombok.Getter;
import lombok.RequiredArgsConstructor;



/**
 * Standardized api error response structure.
 */
@RequiredArgsConstructor
@Getter
@SuppressWarnings({"checkstyle:javadocvariable"})
public final class ErrorResponse {

    private final String requestId;
    private final String error;
    private final String message;

}
