package com.south.african.schools.api.util.resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * A custom class for resource related exceptions.
 */
@RequiredArgsConstructor
@Getter
@SuppressWarnings("checkstyle:javadocvariable")
public class ResourceException extends Throwable {


    @SuppressWarnings({"checkstyle:javadocvariable", "checkstyle:missingjavadoctype"})
    public enum Type {
        RESOURCE_NOT_FOUND
    }

    private final HttpStatus httpStatus;
    private final Type type;
    private final String message;

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static ResourceException resourceNotFound(final String resourceId) {
        final String message = "The resource does not exist";
        return new ResourceException(HttpStatus.NOT_FOUND, Type.RESOURCE_NOT_FOUND, message);
    }
}
