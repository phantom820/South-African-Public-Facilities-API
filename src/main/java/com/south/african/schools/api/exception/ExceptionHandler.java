package com.south.african.schools.api.exception;


import com.south.african.schools.api.util.filter.FilterUtilException;
import com.south.african.schools.api.util.query.QueryException;
import com.south.african.schools.api.util.request.Request;
import com.south.african.schools.api.util.resource.ResourceException;
import com.south.african.schools.api.util.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

/**
 * Exception handler for our api.
 */
@ControllerAdvice
public final class ExceptionHandler {

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @org.springframework.web.bind.annotation.ExceptionHandler(value = QueryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> queryParameterExceptionHandler(
            final QueryException queryParameterException,
            final WebRequest webRequest) {
        final Request request = (Request) webRequest.getAttribute(Request.KEY,  WebRequest.SCOPE_REQUEST);
        return new ResponseEntity<>(new ErrorResponse(request.getId(),
                queryParameterException.getType().toString(),
                queryParameterException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    @org.springframework.web.bind.annotation.ExceptionHandler(value = ResourceException.class)
    public ResponseEntity<ErrorResponse> resourceExceptionHandler(
            final ResourceException resourceException,
            final WebRequest webRequest) {
        final Request request = (Request) webRequest.getAttribute(Request.KEY,  WebRequest.SCOPE_REQUEST);
        return new ResponseEntity<>(new ErrorResponse(request.getId(),
                resourceException.getType().toString(),
                resourceException.getMessage()), resourceException.getHttpStatus());
    }

    @SuppressWarnings("checkstyle:missingjavadocmethod")
    @org.springframework.web.bind.annotation.ExceptionHandler(value = FilterUtilException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> filterUtilExceptionHandler(
            final WebRequest webRequest) {
        final Request request = (Request) webRequest.getAttribute(Request.KEY,  WebRequest.SCOPE_REQUEST);
        return new ResponseEntity<>(new ErrorResponse(request.getId(),
                "INTERNAL_SERVER_ERROR",
                "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

//
//    @SuppressWarnings("checkstyle:missingjavadocmethod")
//    @ExceptionHandler(value = Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseEntity<ErrorResponse> anyExceptionHandler(
//            final WebRequest webRequest) {
//        final Request request = (Request) webRequest.getAttribute(Request.REQUEST_KEY,  WebRequest.SCOPE_REQUEST);
//        return new ResponseEntity<>(new ErrorResponse(request.getRequestId(),
//                "INTERNAL_SERVER_ERROR",
//                "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
