package com.south.african.schools.api.util.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@SuppressWarnings({"checkstyle:javadocvariable", "checkstyle:missingjavadoctype"})
@RequiredArgsConstructor
@Getter
public final class QueryException extends Throwable {

    public enum Type {
        INVALID_PARAMETER_VALUE,
        INVALID_PARAMETER_COMBINATION,
        EMPTY_PARAMETER_VALUE,
        MULTIPLE_PARAMETER_VALUES,
        MISSING_PARAMETER_VALUE,
        UNKNOWN_PARAMETER,
        DUPLICATE_FILTER_KEY,
        UNKNOWN_FILTER_KEY,
        TOO_MANY_FILTER_VALUES,
        NO_FILTER_VALUES,
        INVALID_FILTER_VALUE,
        NO_RESOURCE_ID_VALUES
    }


    private final HttpStatus httpStatus;
    private final Type type;
    private final String message;

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryException invalidParameterValue(final String parameterName, final String value) {
        final String message = "Invalid parameter value : " + value + " , for query parameter : " + parameterName;
        return new QueryException(HttpStatus.BAD_REQUEST, Type.INVALID_PARAMETER_VALUE, message);
    }

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryException invalidFilterValue(final String filterName, final String value) {
        final String message = "Invalid filter value : " + value + " , for query filter : " + filterName;
        return new QueryException(HttpStatus.BAD_REQUEST, Type.INVALID_FILTER_VALUE, message);
    }

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryException invalidParameterCombination(
            final String arg1,
            final String arg2) {
        final String[] args = new String[]{arg1, arg2};
        final String message = "Invalid parameter combination, query parameters : [" + String.join(" ,", args) + "]"
                + "cannot be specified in together";
        return new QueryException(HttpStatus.BAD_REQUEST, Type.INVALID_PARAMETER_COMBINATION, message);
    }

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryException emptyParameterValue(final String parameterName) {
        final String message = "Query parameter : " + parameterName + " , cannot have an empty value";
        return new QueryException(HttpStatus.BAD_REQUEST, Type.EMPTY_PARAMETER_VALUE, message);
    }

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryException multipleParameterValues(final String parameterName) {
        final String message = "Query parameter : " + parameterName + " , cannot have more than one value";
        return new QueryException(HttpStatus.BAD_REQUEST, Type.MULTIPLE_PARAMETER_VALUES, message);
    }

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryException missingParameter(final String parameterName) {
        final String message = "Query parameter : " + parameterName + " , is required";
        return new QueryException(HttpStatus.BAD_REQUEST, Type.MISSING_PARAMETER_VALUE, message);
    }

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryException unknownParameter(final String parameterName) {
        final String message = "Unknown parameter name : " + parameterName;
        return new QueryException(HttpStatus.BAD_REQUEST, Type.UNKNOWN_PARAMETER, message);
    }


    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryException duplicateFilterKey(final String filterKey) {
        final String message = "Filter key : " + filterKey + ", has been specified more than once";
        return new QueryException(HttpStatus.BAD_REQUEST, QueryException.Type.DUPLICATE_FILTER_KEY, message);
    }

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryException unknownFilterKey(final String filterKey) {
        final String message = "Unknown filter key : " + filterKey;
        return new QueryException(HttpStatus.BAD_REQUEST, QueryException.Type.UNKNOWN_FILTER_KEY, message);
    }

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryException tooManyFilterValues(final String filter, final int maximum) {
        final String message = "Too many filter values for filter : " + filter + " , a maximum of " + maximum
                + " values is allowed";
        return new QueryException(HttpStatus.BAD_REQUEST, Type.TOO_MANY_FILTER_VALUES, message);
    }

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryException noFilterValues(final String filter) {
        final String message = "No filter values for filter : " + filter;
        return new QueryException(HttpStatus.BAD_REQUEST, Type.NO_FILTER_VALUES, message);
    }

    @SuppressWarnings({"checkstyle:missingjavadocmethod"})
    public static QueryException noResourceIdValues() {
        final String message = "No resource id values specified";
        return new QueryException(HttpStatus.BAD_REQUEST, Type.NO_RESOURCE_ID_VALUES, message);
    }
}
