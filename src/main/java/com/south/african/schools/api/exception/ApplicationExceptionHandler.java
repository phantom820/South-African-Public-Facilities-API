package com.south.african.schools.api.exception;


import com.south.african.schools.api.util.filter.FilterUtilException;
import com.south.african.schools.api.util.query.QueryParameterException;
import com.south.african.schools.api.util.request.Request;
import com.south.african.schools.api.util.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

/**
 * Exception handler for our api.
 */
@ControllerAdvice
public final class ApplicationExceptionHandler {

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @ExceptionHandler(value = QueryParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> queryParameterExceptionHandler(
            final QueryParameterException queryParameterException,
            final WebRequest webRequest) {
        final Request request = (Request) webRequest.getAttribute(Request.REQUEST_KEY,  WebRequest.SCOPE_REQUEST);
        return new ResponseEntity<>(new ErrorResponse(request.getRequestId(),
                queryParameterException.getType().toString(),
                queryParameterException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @SuppressWarnings("checkstyle:missingjavadocmethod")
    @ExceptionHandler(value = FilterUtilException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> filterUtilExceptionHandler(
            final WebRequest webRequest) {
        final Request request = (Request) webRequest.getAttribute(Request.REQUEST_KEY,  WebRequest.SCOPE_REQUEST);
        return new ResponseEntity<>(new ErrorResponse(request.getRequestId(),
                "INTERNAL_SERVER_ERROR",
                "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @SuppressWarnings("checkstyle:missingjavadocmethod")
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> anyExceptionHandler(
            final WebRequest webRequest) {
        final Request request = (Request) webRequest.getAttribute(Request.REQUEST_KEY,  WebRequest.SCOPE_REQUEST);
        return new ResponseEntity<>(new ErrorResponse(request.getRequestId(),
                "INTERNAL_SERVER_ERROR",
                "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
