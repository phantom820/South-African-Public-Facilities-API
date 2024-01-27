package com.south.african.data.api.util.encoding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.south.african.data.api.util.query.QueryException;
import com.south.african.data.api.util.query.parameter.NextToken;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import java.util.Map;

/**
 * Utility class for creating , encoding and decoding pagination tokens.
 */
@Slf4j
public final class Pagination {

    /**
     * The base 64 encoder for encoding tokens.
     */
    private static final Base64.Encoder ENCODER = Base64.getEncoder();
    /**
     * The base 64 decoder for decoding tokens.
     */
    private static final Base64.Decoder DECODER = Base64.getDecoder();
    /**
     * Serialization.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * Key for resource type in token map.
     */
    private static final String RESOURCE_KEY = "resource";
    /**
     * Key for the underlying cursor.
     */
    private static final String CURSOR_KEY = "cursor";

    private Pagination() { }

    /**
     * Creates a pagination token from the given datastore cursor.
     * @param cursor  A pointer to particular item in a datastore.
     * @return A masked cursor.
     */
    public static String createToken(final String cursor, final String resource) throws JsonProcessingException {
        final Map<String, String> tokenParams = ImmutableMap.of(
                CURSOR_KEY, cursor,
                RESOURCE_KEY, resource);
        final String tokenJson = MAPPER.writeValueAsString(tokenParams);
        return ENCODER.encodeToString(tokenJson.getBytes());
    }

    /**
     * Decodes a given pagination token.
     * @param token  The incoming token form the request,
     * @return decoded token to cursor.
     */
    public static String decodeToken(final String token, final String resource) throws QueryException {
        try {
            final byte[] decodedBytes = DECODER.decode(token);
            final String tokenJson = new String(decodedBytes);
            final Map<String, String> tokenParams = MAPPER.readValue(tokenJson, Map.class);

            if (!tokenParams.containsKey(CURSOR_KEY) || !tokenParams.containsKey(RESOURCE_KEY)) {
                throw QueryException.invalidParameterValue(NextToken.KEY, token);
            } else if (!tokenParams.get(RESOURCE_KEY).equals(resource)) {
                throw QueryException.invalidParameterValue(NextToken.KEY, token);
            }

            return tokenParams.get(CURSOR_KEY);

        } catch (final Exception e) {
            log.error("Failed to decode token {}", token);
            log.error(e.getMessage());
            throw QueryException.invalidParameterValue(NextToken.KEY, token);
        }

    }
}
