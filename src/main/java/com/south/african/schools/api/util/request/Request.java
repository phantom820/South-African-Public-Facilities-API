package com.south.african.schools.api.util.request;


import com.google.common.collect.ImmutableSet;
import com.south.african.schools.api.util.query.QueryParameters;
import lombok.Getter;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Request model.
 */
@Getter
@SuppressWarnings({"checkstyle:javadocvariable"})
public class Request {

    public static final String REQUEST_KEY = "request";
    private final String requestId;
    private final Map<String, ImmutableSet<String>> filters;
    private final QueryParameters.MaxResults maxResults;
    private final QueryParameters.NextToken nextToken;

    /**
     * Constructs a new request with a unique identifier.
     * @param filters       The filters extracted from query parameters.
     * @param maxResults    The max results extracted from query parameters.
     * @param nextToken     The pagination token extracted from query parameters.
     */
    public Request(final Map<String, ImmutableSet<String>> filters,
                   final QueryParameters.MaxResults maxResults,
                   final QueryParameters.NextToken nextToken) {

        this.requestId = generateType1UUID().toString();
        this.filters = filters;
        this.maxResults = maxResults;
        this.nextToken = nextToken;
    }

    private static long get64LeastSignificantBitsForVersion1() {
        Random random = new Random();
        long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
        long variant3BitFlag = 0x8000000000000000L;
        return random63BitLong | variant3BitFlag;
    }

    private static long get64MostSignificantBitsForVersion1() {
        final long currentTimeMillis = System.currentTimeMillis();
        final long timeLow = (currentTimeMillis & 0x0000_0000_FFFF_FFFFL) << 32;
        final long timeMid = ((currentTimeMillis >> 32) & 0xFFFF) << 16;
        final long version = 1 << 12;
        final long timeHi = ((currentTimeMillis >> 48) & 0x0FFF);
        return timeLow | timeMid | version | timeHi;
    }


    @SuppressWarnings("checkstyle:missingjavadocmethod")
    private static UUID generateType1UUID() {
        final long most64SigBits = get64MostSignificantBitsForVersion1();
        final long least64SigBits = get64LeastSignificantBitsForVersion1();
        return new UUID(most64SigBits, least64SigBits);
    }

}
