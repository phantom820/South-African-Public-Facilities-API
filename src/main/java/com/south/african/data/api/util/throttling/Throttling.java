package com.south.african.data.api.util.throttling;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.time.Duration;


@SuppressWarnings({"checkstyle:javadocvariable", "checkstyle:missingjavadoctype", "checkstyle:missingjavadocmethod"})
public final class Throttling {

    private static final int DEFAULT_MINUTELY_BUCKET_CAPACITY = 100;
    private static final int DEFAULT_MINUTELY_REFILL_RATE = 100;

    private Throttling() { }

    public static Bucket defaultThrottlingBucket() {
        final Bucket bucket = Bucket.builder()
                .addLimit(Bandwidth.classic(DEFAULT_MINUTELY_BUCKET_CAPACITY,
                        Refill.intervally(DEFAULT_MINUTELY_REFILL_RATE, Duration.ofMinutes(1))))
                .addLimit(Bandwidth.classic(20, Refill.intervally(20, Duration.ofSeconds(5))))
                .build();
        return bucket;
    }
}
