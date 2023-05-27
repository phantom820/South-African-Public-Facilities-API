package com.south.african.schools.api.util.query;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings(
        { "checkstyle:hideutilityclassconstructor",
          "checkstyle:filetabcharacter",
          "checkstyle:missingjavadoctype",
          "checkstyle:missingJavadocmethod",
          "checkstyle:methodname"})
public class QueryParametersTest {


    @Test
    public void filterKeyPattern_whenKeyMatches() {
        Assert.assertTrue(QueryParameters.FILTER_KEY_PATTERN.matcher("filter-key-1").matches());
        Assert.assertTrue(QueryParameters.FILTER_KEY_PATTERN.matcher("filter-key-55").matches());
    }

    @Test
    public void filterKeyPattern_whenKeyDoesNotMatch() {
        Assert.assertFalse(QueryParameters.FILTER_KEY_PATTERN.matcher("filter-key1").matches());
        Assert.assertFalse(QueryParameters.FILTER_KEY_PATTERN.matcher("filter-key-55adfaf").matches());
    }

    @Test
    public void extractFilters_whenNoFiltersPresent() throws QueryParameterException {

        final Map<String, String[]> params = ImmutableMap.of("maxResults", new String[]{"1000"});
        final Map<String, ImmutableSet<String>> filters = QueryParameters.extractFilters(params);

        Assert.assertTrue(filters.isEmpty());
    }

    @Test
    public void extractFilters_whenFilterPresent_withoutKeyValue() throws QueryParameterException {

        final Map<String, String[]> params = ImmutableMap.of("filter-key-1", new String[]{});

        final QueryParameterException queryParameterException = assertThrows(QueryParameterException.class,
                () -> QueryParameters.extractFilters(params));

        Assert.assertEquals(QueryParameterException.Type.EMPTY_PARAMETER_VALUE, queryParameterException.getType());
        Assert.assertEquals("Query parameter : filter-key-1 , cannot have an empty value",
                queryParameterException.getMessage());

    }

    @Test
    public void extractFilters_whenFilterPresent_withMultipleKeyValues() throws QueryParameterException {

        final Map<String, String[]> params = ImmutableMap.of("filter-key-1", new String[]{"a", "b"});

        final QueryParameterException queryParameterException = assertThrows(QueryParameterException.class,
                () -> QueryParameters.extractFilters(params));

        Assert.assertEquals(QueryParameterException.Type.MULTIPLE_PARAMETER_VALUES, queryParameterException.getType());
        Assert.assertEquals("Query parameter : filter-key-1 , cannot have more than one value",
                queryParameterException.getMessage());

    }

    @Test
    public void extractFilters_whenFilterPresent_and_withoutValues() throws QueryParameterException {

        final Map<String, String[]> params = ImmutableMap.of("filter-key-1", new String[]{"a"});

        final QueryParameterException queryParameterException = assertThrows(QueryParameterException.class,
                () -> QueryParameters.extractFilters(params));

        Assert.assertEquals(QueryParameterException.Type.NO_FILTER_VALUES, queryParameterException.getType());
        Assert.assertEquals("No filter values for filter : a",
                queryParameterException.getMessage());

    }

    @Test
    public void extractFilters_whenFilterDuplicated() throws QueryParameterException {

        final Map<String, String[]> params = ImmutableMap.of(
                "filter-key-1", new String[]{"a"},
                "filter-key-1-value", new String[]{"1"},
                "filter-key-2", new String[]{"a"},
                "filter-key-2-value", new String[]{"2"});

        final QueryParameterException queryParameterException = assertThrows(QueryParameterException.class,
                () -> QueryParameters.extractFilters(params));

        Assert.assertEquals(QueryParameterException.Type.DUPLICATE_FILTER_KEY, queryParameterException.getType());
        Assert.assertEquals("Filter key : a, has been specified more than once",
                queryParameterException.getMessage());

    }

    @Test
    public void extractFilters_whenFilterHasMoreThan50Values() throws QueryParameterException {

        final String[] filterValues = IntStream.range(0, 51).boxed().map(Object::toString).toArray(String[]::new);
        final Map<String, String[]> params = ImmutableMap.of(
                "filter-key-1", new String[]{"a"},
                "filter-key-1-value", filterValues);

        final QueryParameterException queryParameterException = assertThrows(QueryParameterException.class,
                () -> QueryParameters.extractFilters(params));

        Assert.assertEquals(QueryParameterException.Type.TOO_MANY_FILTER_VALUES, queryParameterException.getType());
        Assert.assertEquals("Too many filter values for filter : a , a maximum of 50 values is allowed",
                queryParameterException.getMessage());

    }


    @Test
    public void extractNextToken_whenAbsent() throws QueryParameterException {

        final Map<String, String[]> params = ImmutableMap.of("maxResults", new String[]{"1000"});
        final QueryParameters.NextToken nextToken = QueryParameters.extractNextToken(params);

        Assert.assertTrue(nextToken.isEmpty());
    }

    @Test
    public void extractNextToken_whenSingleValuePresent() throws QueryParameterException {

        final Map<String, String[]> params = ImmutableMap.of("maxResults", new String[]{"1000"},
                "nextToken", new String[]{"abcdefgh"});
        final QueryParameters.NextToken nextToken = QueryParameters.extractNextToken(params);

        Assert.assertFalse(nextToken.isEmpty());
        Assert.assertEquals("abcdefgh", nextToken.getValue());
    }

    @Test
    public void extractNextToken_whenMultipleValuesPresent() throws QueryParameterException {

        final Map<String, String[]> params = ImmutableMap.of("maxResults", new String[]{"1000"},
                "nextToken", new String[]{"abcdefgh", "adad"});

        final QueryParameterException exception = assertThrows(
                QueryParameterException.class, () -> QueryParameters.extractNextToken(params));

        Assert.assertEquals(QueryParameterException.Type.MULTIPLE_PARAMETER_VALUES, exception.getType());

    }

    @Test
    public void extractMaxResults_whenAbsent() throws QueryParameterException {

        final Map<String, String[]> params = ImmutableMap.of("nextToken", new String[]{"afafaf"});
        final QueryParameters.MaxResults maxResults = QueryParameters.extractMaxResults(params);

        Assert.assertTrue(maxResults.isEmpty());
    }

    @Test
    public void extractMaxResults_whenSingleValuePresent() throws QueryParameterException {

        final Map<String, String[]> params = ImmutableMap.of("maxResults", new String[]{"1000"},
                "nextToken", new String[]{"abcdefgh"});
        final QueryParameters.MaxResults maxResults = QueryParameters.extractMaxResults(params);

        Assert.assertFalse(maxResults.isEmpty());
        Assert.assertEquals(new Integer(1000), maxResults.getValue());
    }

    @Test
    public void extractMaxResults_whenMultipleValuesPresent() throws QueryParameterException {

        final Map<String, String[]> params = ImmutableMap.of("maxResults", new String[]{"1000", "200"},
                "nextToken", new String[]{"abcdefgh"});

        final QueryParameterException exception = assertThrows(
                QueryParameterException.class, () -> QueryParameters.extractMaxResults(params));

        Assert.assertEquals(QueryParameterException.Type.MULTIPLE_PARAMETER_VALUES, exception.getType());

    }

    @Test
    public void extractMaxResults_whenValueIsMalformed() throws QueryParameterException {

        final Map<String, String[]> params = ImmutableMap.of("maxResults", new String[]{"1000a"},
                "nextToken", new String[]{"abcdefgh"});

        final QueryParameterException exception = assertThrows(
                QueryParameterException.class, () -> QueryParameters.extractMaxResults(params));

        Assert.assertEquals(QueryParameterException.Type.INVALID_PARAMETER_VALUE, exception.getType());

    }

    @Test
    public void extractMaxResults_whenValueIsLessThanOne() throws Exception {

        final Map<String, String[]> params = ImmutableMap.of("maxResults", new String[]{"0"},
                "nextToken", new String[]{"abcdefgh"});

        final QueryParameterException exception = assertThrows(
                QueryParameterException.class, () -> QueryParameters.extractMaxResults(params));

        Assert.assertEquals(QueryParameterException.Type.INVALID_PARAMETER_VALUE, exception.getType());

    }

    @Test
    public void extractMaxResults_whenValueIsGreaterThanOneThousand() throws Exception {

        final Map<String, String[]> params = ImmutableMap.of("maxResults", new String[]{"1001"},
                "nextToken", new String[]{"abcdefgh"});

        final QueryParameterException exception = assertThrows(
                QueryParameterException.class, () -> QueryParameters.extractMaxResults(params));

        Assert.assertEquals(QueryParameterException.Type.INVALID_PARAMETER_VALUE, exception.getType());

    }

    @Test
    public void validateParameters_unknownParametersPresent() {

        final Map<String, String[]> params = ImmutableMap.of("maxResults", new String[]{"1001"},
                "nextToken", new String[]{"abcdefgh"}, "filter", new String[]{});

        final QueryParameterException exception = assertThrows(
                QueryParameterException.class, () -> QueryParameters.validateParameters(params));

        Assert.assertEquals(QueryParameterException.Type.UNKNOWN_PARAMETER_NAME, exception.getType());
    }


    @Test
    public void validateParameters() {

        final Map<String, String[]> params = ImmutableMap.of("maxResults", new String[]{"1001"},
                "nextToken", new String[]{"abcdefgh"}, "filter-key-1", new String[]{});

        assertDoesNotThrow(() -> QueryParameters.validateParameters(params));

    }
}
