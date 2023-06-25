package com.south.african.schools.api.util.query.parameter;

import com.google.common.annotations.VisibleForTesting;
import com.south.african.schools.api.util.query.QueryException;

import java.util.Map;

/**
 * Represents the maximum number of results specified in request query string.
 */
@SuppressWarnings("checkstyle:javadocvariable")
public final class MaxResult {

    public static final String KEY = "maxResults";

    public static final Integer DEFAULT_MAX_RESULT = 500;

    private final Integer maxResult;

    /**
     * Creates the max results derived from the query parameters of a request.
     * @param parameters The request query parameters.
     */
    public MaxResult(final Map<String, String[]> parameters) throws QueryException {
        this.maxResult = extractMaxResult(parameters);
    }

    @VisibleForTesting
    Integer extractMaxResult(final Map<String, String[]> parameters) throws QueryException {
        if (parameters == null) {
            return null;
        } else if (!parameters.containsKey(KEY)) {
            return null;
        } else if (parameters.get(KEY).length > 1) {
            throw QueryException.multipleParameterValues(KEY);
        } else if ((parameters.get(KEY)[0]).isEmpty()) {
            throw QueryException.emptyParameterValue(KEY);
        } else {
            try  {
                final int maxResult = Integer.parseInt(parameters.get(KEY)[0]);
                if (maxResult < 1 || maxResult > 1000) {
                    throw QueryException.invalidParameterValue(KEY, parameters.get(KEY)[0]);
                }
                return maxResult;
            } catch (Exception e) {
                throw QueryException.invalidParameterValue(KEY, parameters.get(KEY)[0]);
            }
        }
    }


    /**
     * Checks if there is no next token value.
     * @return true if there is no next token.
     */
    public boolean isEmpty() {
        return maxResult == null;
    }


    /**
     * Returns the value of max results.
     * @return maximum number of results allowed in a request.
     */
    public Integer value() {
        return this.maxResult;
    }
}
