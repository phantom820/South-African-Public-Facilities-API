package com.south.african.data.api.util.request;


import com.south.african.data.api.util.metrics.Metrics;
import lombok.Getter;
import java.util.Random;
import java.util.UUID;

/**
 * Request model.
 */
@Getter
@SuppressWarnings({"checkstyle:javadocvariable"})
public final class Request {

    public static final String KEY = "request";
    private final String id;
    private final Metrics metrics;
    private final long startTimeMillis = System.currentTimeMillis();
    private long endTimeMillis;


    /**
     * Constructs a new request with a unique identifier.
     */
    public Request() {
        this.id = generateType1UUID().toString();
        this.metrics = new Metrics();
    }

    private static long get64LeastSignificantBitsForVersion1() {
        final Random random = new Random();
        final long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
        final long variant3BitFlag = 0x8000000000000000L;
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

    private static UUID generateType1UUID() {
        final long most64SigBits = get64MostSignificantBitsForVersion1();
        final long least64SigBits = get64LeastSignificantBitsForVersion1();
        return new UUID(most64SigBits, least64SigBits);
    }

    /**
     * Sets the end time of the request in milliseconds to given value.
     * @param endTimeMillis
     */
    public void setEndTimeMillis(final long endTimeMillis) {
        this.endTimeMillis = endTimeMillis;
    }
}
