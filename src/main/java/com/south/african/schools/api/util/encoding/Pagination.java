package com.south.african.schools.api.util.encoding;

import java.util.Base64;

/**
 * Utility class for creating , encoding and decoding pagination tokens.
 */
public final class Pagination {

    /**
     * The base 64 encoder for encoding tokens.
     */
    private static final Base64.Encoder ENCODER = Base64.getEncoder();
    /**
     * The bas 64 decoder for decoding tokens.
     */
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    private Pagination() { }

    /**
     * Creates a pagination from the given datastore cursor.
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
    public static String decodeToken(final String token) {
        final byte[] decodedBytes = DECODER.decode(token);
        return new String(decodedBytes);
    }
}
