package com.south.african.schools.api.util.query;


import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

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
