package com.south.african.schools.api.util.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Custom exception for query parameter related exceptions.
 */
@RequiredArgsConstructor
@Getter
@SuppressWarnings("checkstyle:javadocvariable")
public class QueryParameterException extends Throwable {

    @SuppressWarnings("checkstyle:javadocvariable")
    enum Type {
        INVALID_PARAMETER_VALUE,
        MULTIPLE_PARAMETER_VALUES,
        UNKNOWN_PARAMETER_NAME,
        DUPLICATE_FILTER_KEY,
    }

    private final HttpStatus httpStatus;
    private final Type type;
    private final String message;

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryParameterException invalidParameterValue(final String parameterName, final String value) {
        final String message = "Invalid parameter value : " + value + ", for query parameter : " + parameterName;
        return new QueryParameterException(HttpStatus.BAD_REQUEST, Type.INVALID_PARAMETER_VALUE, message);
    }

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryParameterException emptyParameterValue(final String parameterName) {
        final String message = "Query parameter : " + parameterName + ", cannot have an empty value";
        return new QueryParameterException(HttpStatus.BAD_REQUEST, Type.INVALID_PARAMETER_VALUE, message);
    }

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryParameterException multipleParameterValues(final String parameterName) {
        final String message = "Query parameter : " + parameterName + ", cannot have more than one value";
        return new QueryParameterException(HttpStatus.BAD_REQUEST, Type.MULTIPLE_PARAMETER_VALUES, message);
    }

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryParameterException unknownParameterName(final String parameterName) {
        final String message = "Unknown parameter name : " + parameterName;
        return new QueryParameterException(HttpStatus.BAD_REQUEST, Type.UNKNOWN_PARAMETER_NAME, message);
    }

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryParameterException duplicateFilterKey(final String filterKey) {
        final String message = "Filter key : " + filterKey + ", has been specified more than once.";
        return new QueryParameterException(HttpStatus.BAD_REQUEST, Type.DUPLICATE_FILTER_KEY, message);
    }
}
