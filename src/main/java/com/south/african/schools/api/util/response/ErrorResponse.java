package com.south.african.schools.api.util.response;


import org.json.simple.JSONObject;



/**
 * Standardized api error response structure.
 */
@SuppressWarnings({"checkstyle:javadocvariable"})
public class ErrorResponse extends Response {



    @SuppressWarnings({"checkstyle:missingJavadocmethod"})
    public ErrorResponse(final String requestId, final JSONObject data) {
        super(requestId, null, null);
    }
}
