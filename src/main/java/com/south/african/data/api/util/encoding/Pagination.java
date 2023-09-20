package com.south.african.data.api.util.encoding;

import com.south.african.data.api.util.query.QueryException;
import com.south.african.data.api.util.query.parameter.NextToken;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

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

    private Pagination() { }

    /**
     * Creates a pagination token from the given datastore cursor.
     * @param cursor  A pointer to particular item in a datastore.
     * @return A masked cursor.
     */
    public static String createToken(final String cursor) {
        return ENCODER.encodeToString(cursor.getBytes());
    }

    /**
     * Decodes a given pagination token.
     * @param token  The incoming token form the request,
     * @return decoded token to cursor.
     */
    public static String decodeToken(final String token) throws QueryException {
        try {
            final byte[] decodedBytes = DECODER.decode(token);
            return new String(decodedBytes);
        } catch (final Exception e) {
            log.error("Failed to decode token {}", token);
            log.error(e.getMessage());
            throw QueryException.invalidParameterValue(NextToken.KEY, token);
        }

    }
}
