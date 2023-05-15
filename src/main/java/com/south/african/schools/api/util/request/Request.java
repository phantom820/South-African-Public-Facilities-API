package com.south.african.schools.api.util.request;


import com.google.common.collect.ImmutableSet;
import com.south.african.schools.api.util.query.QueryParameters;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Request model.
 */
@RequiredArgsConstructor
@Getter
@SuppressWarnings({"checkstyle:javadocvariable"})
public class Request {

    public static final String REQUEST_KEY = "request";
    private final String requestId;
    private final Map<String, ImmutableSet<String>> filters;
    private final QueryParameters.MaxResults maxResults;
    private final QueryParameters.NextToken nextToken;

}
