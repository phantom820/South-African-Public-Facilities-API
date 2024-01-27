package com.south.african.data.api.util.query.parameter;

import com.google.common.annotations.VisibleForTesting;
import com.south.african.data.api.util.query.QueryException;

import java.util.Map;

/**
 * Represents the maximum number of results specified in request query string.
 */
@SuppressWarnings("checkstyle:javadocvariable")
public final class MaxResults {
    public static final String DESCRIPTION =
            "The maximum number of items to return in a single request. The range of allowed values is [5, 1000].";

    public static final String KEY = "maxResults";

    public static final Integer DEFAULT_MAX_RESULTS = 500;

    private final Integer maxResults;

    /**
     * Creates the max results derived from the query parameters of a request.
     * @param parameters The request query parameters.
     */
    public MaxResults(final Map<String, String[]> parameters) throws QueryException {
        this.maxResults = extractMaxResult(parameters);
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
        return maxResults == null;
    }


    /**
     * Returns the value of max results.
     * @return maximum number of results allowed in a request.
     */
    public Integer value() {
        return this.maxResults;
    }
}
