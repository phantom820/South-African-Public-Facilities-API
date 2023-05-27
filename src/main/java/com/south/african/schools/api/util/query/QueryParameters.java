package com.south.african.schools.api.util.query;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utility class for extracting and validating query parameters.
 */
@SuppressWarnings("checkstyle:javadocvariable")
public final class QueryParameters {

    private QueryParameters() { }

    public static final String MAX_RESULTS_KEY = "maxResults";
    public static final String NEXT_TOKEN_KEY = "nextToken";
    public static final Pattern FILTER_KEY_PATTERN = Pattern.compile("^filter-key-\\d+$");
    public static final Pattern FILTER_VALUE_PATTERN = Pattern.compile("^filter-key-\\d+-value$");

    static final int MAX_FILTER_VALUES = 50;

    /**
     * Represents a next token for pagination.
     */
    @RequiredArgsConstructor
    @Getter
    public static final class NextToken {
        /**
         * The key indicating the parameter to be the next token.
         */
        private final String value;

        /**
         * Returns true if there is no next token value.
         * @return true if no value.
         */
        public boolean isEmpty() {
            return value == null || value.isEmpty();
        }
    }

    /**
     * Represents max results for pagination.
     */
    @RequiredArgsConstructor
    @Getter
    public static final class MaxResults {

        private final Integer value;

        /**
         * Returns true if there is no next token value.
         * @return true if no value.
         */
        public boolean isEmpty() {
            return value == null;
        }
    }

    /**
     * Extracts the next token query parameter value for a request.
     * @param parameters The query parameters map for a request.
     * @return The next token value extracted from the request.
     * @throws QueryParameterException
     */
    public static NextToken extractNextToken(final Map<String, String[]> parameters) throws QueryParameterException {
        if (parameters == null) {
            return new NextToken(null);
        } else if (!parameters.containsKey(NEXT_TOKEN_KEY)) {
            return  new NextToken(null);
        } else if (parameters.get(NEXT_TOKEN_KEY).length > 1) {
            throw QueryParameterException.multipleParameterValues(NEXT_TOKEN_KEY);
        } else if ((parameters.get(NEXT_TOKEN_KEY)[0]).isEmpty()) {
            throw QueryParameterException.emptyParameterValue(NEXT_TOKEN_KEY);
        }

        return new NextToken((parameters.get(NEXT_TOKEN_KEY)[0]));
    }

    /**
     * Extracts the max results query parameter value for a request.
     * @param parameters The query parameters map for a request.
     * @return The max results value extracted from the request.
     * @throws QueryParameterException
     */
    public static MaxResults extractMaxResults(final Map<String, String[]> parameters) throws QueryParameterException {
        if (parameters == null) {
            return new MaxResults(null);
        } else if (!parameters.containsKey(MAX_RESULTS_KEY)) {
            return  new MaxResults(null);
        } else if (parameters.get(MAX_RESULTS_KEY).length > 1) {
            throw QueryParameterException.multipleParameterValues(MAX_RESULTS_KEY);
        } else if ((parameters.get(MAX_RESULTS_KEY)[0]).isEmpty()) {
            throw QueryParameterException.emptyParameterValue(MAX_RESULTS_KEY);
        } else {
            try  {
                final int maxResults = Integer.parseInt(parameters.get(MAX_RESULTS_KEY)[0]);
                if (maxResults < 1 || maxResults > 1000) {
                    throw QueryParameterException.invalidParameterValue(MAX_RESULTS_KEY, parameters.get(MAX_RESULTS_KEY)[0]);
                }
                return new MaxResults(maxResults);
            } catch (Exception e) {
                throw QueryParameterException.invalidParameterValue(MAX_RESULTS_KEY, parameters.get(MAX_RESULTS_KEY)[0]);
            }
        }
    }


    /**
     * Validates if query parameters consist of known/allowed values.
     * @param parameters The query parameters map for a request.
     * @throws QueryParameterException
     */
    public static void validateParameters(final Map<String, String[]> parameters) throws QueryParameterException {

        if (parameters == null || parameters.isEmpty()) {
            return;
        }
        for (final String key : parameters.keySet()) {
            if (!key.equals(MAX_RESULTS_KEY) && !key.equals(NEXT_TOKEN_KEY)) {
                if (!FILTER_KEY_PATTERN.matcher(key).matches() && !FILTER_VALUE_PATTERN.matcher(key).matches()) {
                    throw QueryParameterException.unknownParameterName(key);
                }
            }
        }
    }

    /**
     * Extracts the filters for a request.
     * @param parameters The query parameters map for a request.
     * @return A map of filters.
     * @throws QueryParameterException
     */
    public static Map<String, ImmutableSet<String>> extractFilters(final Map<String, String[]> parameters)
            throws QueryParameterException {

        if (parameters == null || parameters.isEmpty()) {
            return ImmutableMap.of();
        }

        final Map<String, ImmutableSet<String>> filters = new HashMap<>();

        for (final String key : parameters.keySet()) {
            if (FILTER_KEY_PATTERN.matcher(key).matches()) {
                if (parameters.get(key).length == 0) {
                    throw QueryParameterException.emptyParameterValue(key);
                } else if (parameters.get(key).length > 1) {
                    throw QueryParameterException.multipleParameterValues(key);
                } else if (parameters.get(key)[0] == null || parameters.get(key)[0].isEmpty()) {
                    throw QueryParameterException.invalidParameterValue(key, parameters.get(key)[0]);
                } else {
                    final String valueKey = key + "-value";
                    if (filters.containsKey(parameters.get(key)[0])) {
                        throw QueryParameterException.duplicateFilterKey(parameters.get(key)[0]);
                    } else if (!parameters.containsKey(valueKey)) {
                        throw QueryParameterException.noFilterValues(parameters.get(key)[0]);
                    } else if (parameters.get(valueKey).length > MAX_FILTER_VALUES) {
                        throw QueryParameterException.tooManyFilterValues(parameters.get(key)[0], MAX_FILTER_VALUES);
                    }
                    filters.put(parameters.get(key)[0],
                            Arrays.stream(parameters.get(valueKey)).collect(ImmutableSet.toImmutableSet()));
                }
            }
        }

        return  ImmutableMap.copyOf(filters);
    }

}
