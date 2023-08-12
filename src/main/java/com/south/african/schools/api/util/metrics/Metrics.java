package com.south.african.schools.api.util.metrics;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Metrics that are attached to each request. There are count metrics (these are cumulative for same key) and value metrics,
 * which generate list of values.
 */
public final class Metrics {

    /**
     * Count metrics , these are additive based on the key.
     */
    @JsonProperty
    private final Map<String, Integer> count;

    /**
     * Individual metric values for particular key.
     */
    @JsonProperty
    private final Map<String, List<Double>> value;

    /**
     * The top level latency metric in milliseconds.
     */
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long latencyMillis;

    /**
     * Metrics constructor.
     */
    public Metrics() {
        this.count = new HashMap<>();
        this.value = new HashMap<>();
    }

    /**
     * This adds a count metric for the given metricName. Will accumulate value if metric metricName is already present.
     * @param metricName   The metric name to add a count value to.
     * @param metricCount  The count value to add.
     */
    public void addCount(final String metricName, final int metricCount) {
        count.compute(metricName, (k, v) -> (v == null) ? metricCount : v + metricCount);
    }

    /**
     * This adds a value metric for the given key.
     * @param metricName   The metric name to add a count value to.
     * @param metricValue  The value to be appended to list of metric values.
     */
    public void addValue(final String metricName, final double metricValue) {
        if (value.containsKey(metricName)) {
            value.get(metricName).add(metricValue);
            return;
        }
        final ArrayList<Double> newValues = new ArrayList<>();
        newValues.add(metricValue);
        value.put(metricName, newValues);
    }

    /**
     * Sets the latency metric to the given value.
     * @param latencyMillis The latency value to set to.
     */
    public void setLatencyMillis(final long latencyMillis) {
        this.latencyMillis = latencyMillis;
    }
}

