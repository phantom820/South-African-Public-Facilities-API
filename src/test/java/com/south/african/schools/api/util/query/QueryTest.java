package com.south.african.schools.api.util.query;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.south.african.schools.api.util.query.parameter.MaxResults;
import com.south.african.schools.api.util.query.parameter.NextToken;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings(
        { "checkstyle:hideutilityclassconstructor",
                "checkstyle:filetabcharacter",
                "checkstyle:missingjavadoctype",
                "checkstyle:missingJavadocmethod",
                "checkstyle:methodname"})
public class QueryTest {

    @Test
    public void query_paginated() throws QueryException {

        final Map<String, String[]> params = ImmutableMap.of(
                "nextToken", new String[]{"A"}, "maxResults", new String[]{"100"});

        final Query query = new Query(
                null,
                new MaxResults(params),
                new NextToken(params));

        assertTrue(query.isPaginated());
        assertTrue(query.hasNextToken());
    }

    @Test
    public void extractFilters_whenParametersNull() throws QueryException {

        final Map<String, ImmutableSet<String>> filters = Query.extractFilters(null);
        assertTrue(filters.isEmpty());
    }

    @Test
    public void extractFilters_whenParametersEmpty() throws QueryException {

        final Map<String, ImmutableSet<String>> filters = Query.extractFilters(ImmutableMap.of());
        assertTrue(filters.isEmpty());
    }

    @Test
    public void extractFilters_whenFilterKey_isEmpty() {

        final QueryException exception = assertThrows(QueryException.class,
                () -> Query.extractFilters(ImmutableMap.of("filter-key-1", new String[]{})));

        assertEquals(QueryException.Type.EMPTY_PARAMETER_VALUE, exception.getType());
    }

    @Test
    public void extractFilters_whenFilterKey_isNull() {

        final Map<String, String[]> parameters = new HashMap<>();
        parameters.put("filter-key-1", null);

        final QueryException exception = assertThrows(QueryException.class,
                () -> Query.extractFilters(parameters));

        assertEquals(QueryException.Type.EMPTY_PARAMETER_VALUE, exception.getType());

    }

    @Test
    public void extractFilters_whenFilterKey_hasMultipleValues() {

        final QueryException exception = assertThrows(QueryException.class,
                () -> Query.extractFilters(ImmutableMap.of("filter-key-1", new String[]{"A", "B"})));

        assertEquals(QueryException.Type.MULTIPLE_PARAMETER_VALUES, exception.getType());
    }

    @Test
    public void extractFilters_whenFilterKey_hasNullValue() {

        final QueryException exception = assertThrows(QueryException.class,
                () -> Query.extractFilters(ImmutableMap.of("filter-key-1", new String[]{null})));

        assertEquals(QueryException.Type.INVALID_PARAMETER_VALUE, exception.getType());
    }

    @Test
    public void extractFilters_whenFilterKey_hasEmptyValue() {

        final QueryException exception = assertThrows(QueryException.class,
                () -> Query.extractFilters(ImmutableMap.of("filter-key-1", new String[]{""})));
        assertEquals(QueryException.Type.INVALID_PARAMETER_VALUE, exception.getType());
    }

    @Test
    public void extractFilters_whenFilterKey_hasNoValues() {

        final QueryException exception = assertThrows(QueryException.class,
                () -> Query.extractFilters(ImmutableMap.of("filter-key-1", new String[]{"A"})));

        assertEquals(QueryException.Type.NO_FILTER_VALUES, exception.getType());
    }

    @Test
    public void extractFilters_whenFilterKey_hasValues() throws QueryException {

        final Map<String, ImmutableSet<String>> filters = Query.extractFilters(ImmutableMap.of(
                        "filter-key-1", new String[]{"filterOne"},
                        "filter-key-1-value", new String[]{"A", "B"},
                        "filter-key-2", new String[]{"filterTwo"},
                        "filter-key-2-value", new String[]{"C", "D"}));

        assertEquals(ImmutableSet.of("A", "B"), filters.get("filterOne"));
        assertEquals(ImmutableSet.of("C", "D"), filters.get("filterTwo"));
    }

    @Test
    public void extractFilters_whenFilterKey_duplicated() {

        final QueryException exception = assertThrows(QueryException.class,
                () -> Query.extractFilters(ImmutableMap.of(
                        "filter-key-1", new String[]{"filterOne"},
                        "filter-key-1-value", new String[]{"A", "B"},
                        "filter-key-2", new String[]{"filterOne"},
                        "filter-key-2-value", new String[]{"C", "D"})));

        assertEquals(QueryException.Type.DUPLICATE_FILTER_KEY, exception.getType());
    }

    @Test
    public void extractFilters_whenFilterKey_hasValues_exceedingValuesLimit() throws QueryException {

        final String[] filterValues = IntStream.range(1, 52).boxed().map(i -> i.toString())
                .toArray(size -> new String[size]);

        final QueryException exception = assertThrows(QueryException.class, () -> Query.extractFilters(ImmutableMap.of(
                "filter-key-1", new String[]{"filterOne"},
                "filter-key-1-value", filterValues)));

        assertEquals(QueryException.Type.TOO_MANY_FILTER_VALUES, exception.getType());
    }

    @Test
    public void extractResourceIds_whenParametersNull() throws QueryException {

        final Set<String> resourceIds = Query.extractResourceIds(null);
        assertTrue(resourceIds.isEmpty());
    }

    @Test
    public void extractResourceIds_whenParametersEmpty() throws QueryException {

        final Set<String> resourceIds = Query.extractResourceIds(ImmutableMap.of());
        assertTrue(resourceIds.isEmpty());
    }

    @Test
    public void extractResourceIds_whenResourceIds_isEmpty() throws QueryException {

        final QueryException exception = assertThrows(QueryException.class, () -> Query.extractResourceIds(ImmutableMap.of(
                "resourceId", new String[]{})));

        assertEquals(QueryException.Type.NO_RESOURCE_ID_VALUES, exception.getType());
    }

    @Test
    public void extractResourceIds_whenResourceIds_isNull() throws QueryException {

        final Map<String, String[]> parameters = new HashMap<>();
        parameters.put("resourceId", null);

        final QueryException exception = assertThrows(QueryException.class, () -> Query.extractResourceIds(parameters));
        assertEquals(QueryException.Type.NO_RESOURCE_ID_VALUES, exception.getType());
    }

    @Test
    public void extractResourceIds_whenResourceIds_present() throws QueryException {

        final Set<String> resourceIds = Query.extractResourceIds(ImmutableMap.of("resourceId", new String[]{"a", "b"}));
        assertEquals(ImmutableSet.of("a", "b"), resourceIds);
    }

    @Test
    public void validateParameters_nextToken_withoutMaxResults() {

        final QueryException exception = assertThrows(QueryException.class, () -> Query.validateParameters(
                ImmutableMap.of("nextToken", new String[]{"A"})));

        assertEquals(QueryException.Type.MISSING_PARAMETER_VALUE, exception.getType());

    }

    @Test
    public void validateParameters_nextToken_withMaxResults() {

        assertDoesNotThrow(() -> Query.validateParameters(
                ImmutableMap.of("nextToken", new String[]{"A"}, "maxResults", new String[]{"100"})));
    }

    @Test
    public void validateParameters_nextToken_withResourceIds() {

        final QueryException exception = assertThrows(QueryException.class, () -> Query.validateParameters(
                ImmutableMap.of(
                        "nextToken", new String[]{"A"},
                        "maxResults", new String[]{"200"},
                        "resourceId", new String[]{"A"})));

        assertEquals(QueryException.Type.INVALID_PARAMETER_COMBINATION, exception.getType());

    }

    @Test
    public void validateParameters_unknownParameter() {

        final QueryException exception = assertThrows(QueryException.class, () -> Query.validateParameters(
                ImmutableMap.of(
                        "nextToken", new String[]{"A"},
                        "maxResults", new String[]{"200"},
                        "someParam", new String[]{"A"})));

        assertEquals(QueryException.Type.UNKNOWN_PARAMETER, exception.getType());

    }
}
