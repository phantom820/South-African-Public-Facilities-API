package com.south.african.schools.api.util.query.parameter;


import com.google.common.annotations.VisibleForTesting;
import com.south.african.schools.api.util.query.QueryException;;
import java.util.Map;

/**
 * Represents the next token specified in request query string for pagination.
 */
@SuppressWarnings("checkstyle:javadocvariable")
public class NextToken {

    public static final String KEY = "nextToken";

    private final String nextToken;

    /**
     * Creates the next token derived from the query parameters of a request.
     * @param parameters The request query parameters.
     */
    public NextToken(final Map<String, String[]> parameters) throws QueryException {
        this.nextToken = extractNextToken(parameters);
    }

    @VisibleForTesting
    String extractNextToken(final Map<String, String[]> parameters) throws QueryException {
        if (parameters == null) {
            return null;
        } else if (!parameters.containsKey(KEY)) {
            return  null;
        } else if (parameters.get(KEY).length > 1) {
            QueryException.multipleParameterValues(KEY);
        } else if ((parameters.get(KEY)[0]).isEmpty()) {
            throw QueryException.emptyParameterValue(KEY);
        }

        return parameters.get(KEY)[0];
    }

    /**
     * Checks if there is no next token value.
     * @return true if there is no next token.
     */
    public boolean isEmpty() {
        return nextToken == null || nextToken.isEmpty();
    }

    /**
     * Returns the pagination token .
     * @return next token for pagination.
     */
    public String value() {
        return this.nextToken;
    }
}
