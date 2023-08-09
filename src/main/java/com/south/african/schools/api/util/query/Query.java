package com.south.african.schools.api.util.query;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.south.african.schools.api.util.query.parameter.MaxResult;
import com.south.african.schools.api.util.query.parameter.NextToken;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Represents the request query.
 */
@SuppressWarnings("checkstyle:javadocvariable")
@Getter
public final class Query {

    public static final String KEY = "query";
    public static final Pattern FILTER_KEY_PATTERN = Pattern.compile("^filter-key-\\d+$");
    public static final Pattern FILTER_VALUE_PATTERN = Pattern.compile("^filter-key-\\d+-value$");
    public static final String RESOURCE_ID_KEY = "resourceId";
    public static final int MAX_FILTER_VALUES = 50;
    public static final int MAX_RESOURCE_ID_VALUES = 100;
    private final MaxResult maxResult;
    private final NextToken nextToken;
    private final Map<String, ImmutableSet<String>> filters;

    /**
     * Creates a query from the given request query parameters.
     * @param filters       The filters associated with the query.
     * @param maxResult     The max results allowed for the query.
     * @param nextToken     The next token for pagination.
     */
    public Query(final Map<String, ImmutableSet<String>> filters, final MaxResult maxResult, final NextToken nextToken) {
        this.maxResult = maxResult;
        this.nextToken = nextToken;
        this.filters = filters;
    }

    /**
     * Extracts the filters for a request.
     * @param parameters The query parameters map for a request.
     * @return A map of filters.
     * @throws QueryException
     */
    public static Map<String, ImmutableSet<String>> extractFilters(final Map<String, String[]> parameters)
            throws QueryException {

        if (parameters == null || parameters.isEmpty()) {
            return ImmutableMap.of();
        }

        final Map<String, ImmutableSet<String>> filters = new HashMap<>();

        for (final String key : parameters.keySet()) {
            if (FILTER_KEY_PATTERN.matcher(key).matches()) {
                if (parameters.get(key) == null ||  parameters.get(key).length == 0) {
                    throw QueryException.emptyParameterValue(key);
                } else if (parameters.get(key).length > 1) {
                    throw QueryException.multipleParameterValues(key);
                } else if (parameters.get(key)[0] == null || parameters.get(key)[0].isEmpty()) {
                    throw QueryException.invalidParameterValue(key, parameters.get(key)[0]);
                } else {
                    final String valueKey = key + "-value";
                    if (filters.containsKey(parameters.get(key)[0])) {
                        throw QueryException.duplicateFilterKey(parameters.get(key)[0]);
                    } else if (!parameters.containsKey(valueKey)) {
                        throw QueryException.noFilterValues(parameters.get(key)[0]);
                    } else if (parameters.get(valueKey).length > MAX_FILTER_VALUES) {
                        throw QueryException.tooManyFilterValues(parameters.get(key)[0], MAX_FILTER_VALUES);
                    }
                    filters.put(parameters.get(key)[0],
                            Arrays.stream(parameters.get(valueKey)).collect(ImmutableSet.toImmutableSet()));
                }
            }
        }

        return  ImmutableMap.copyOf(filters);
    }


    /**
     * Extracts the resource ids for a request.
     * @param parameters The query parameters map for a request.
     * @return An immutable set of resource ids.
     * @throws QueryException
     */
    public static ImmutableSet<String> extractResourceIds(final Map<String, String[]> parameters) throws QueryException {
        if (parameters == null || parameters.isEmpty() || !parameters.containsKey(RESOURCE_ID_KEY)) {
            return ImmutableSet.of();
        } else if (parameters.get(RESOURCE_ID_KEY) == null  || parameters.get(RESOURCE_ID_KEY).length == 0) {
            throw QueryException.noResourceIdValues();
        }

        return Arrays.stream(parameters.get(RESOURCE_ID_KEY))
                .collect(ImmutableSet.toImmutableSet());
    }

    /**
     * Check whether the query is paginated or not. A paginated query has a non empty max results value.
     * @return true if the query has a value for max results.
     */
    public boolean isPaginated() {
        return !this.maxResult.isEmpty();
    }


    /**
     * Check whether the query has a next token.
     * @return true if the query has a value for next token.
     */
    public boolean hasNextToken() {
        return !this.nextToken.isEmpty();
    }

    /**
     * Validates if query parameters consist of allowed values and combinations.
     * @param parameters The query parameters map for a request.
     * @throws QueryException
     */
    public static void validateParameters(final Map<String, String[]> parameters) throws QueryException {

        if (parameters == null || parameters.isEmpty()) {
            return;
        } else if (parameters.containsKey(NextToken.KEY) && !parameters.containsKey(MaxResult.KEY)) {
            throw QueryException.missingParameter(MaxResult.KEY);
        } else if (parameters.containsKey(MaxResult.KEY) && parameters.containsKey(RESOURCE_ID_KEY)) {
            throw QueryException.invalidParameterCombination(RESOURCE_ID_KEY, MaxResult.KEY);
        }

        for (final String key : parameters.keySet()) {
            if (!key.equals(MaxResult.KEY) && !key.equals(NextToken.KEY) && !key.equals(RESOURCE_ID_KEY)) {
                if (!FILTER_KEY_PATTERN.matcher(key).matches() && !FILTER_VALUE_PATTERN.matcher(key).matches()) {
                    throw QueryException.unknownParameter(key);
                }
            }
        }
    }

}
