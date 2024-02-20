package com.south.african.data.api.controller;

import com.south.african.data.api.util.query.QueryException;
import com.south.african.data.api.entity.School;
import com.south.african.data.api.service.SchoolService;
import com.south.african.data.api.util.filter.FilterUtil;
import com.south.african.data.api.util.query.Query;
import com.south.african.data.api.util.query.parameter.MaxResults;
import com.south.african.data.api.util.query.parameter.NextToken;
import com.south.african.data.api.util.request.Request;
import com.south.african.data.api.util.resource.ResourceException;
import com.south.african.data.api.util.response.Response;
import com.south.african.data.api.util.throttling.Throttling;
import io.github.bucket4j.Bucket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for different endpoints that have a prefix of schools/ .
 */
@CrossOrigin(
        allowCredentials = "false",
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.GET}
)
@SuppressWarnings("unchecked")
@RestController
public class SchoolController {

    /**
     * The school service for obtaining school data.
     */
    @Autowired
    private SchoolService schoolService;

    /**
     * The default throttling bucket.
     */
    private final Bucket bucket = Throttling.defaultThrottlingBucket();

    @Operation(summary = "Retrieves schools that satisfy given query parameters.")
    @GetMapping(value = "/schools", produces = "application/json")
    ResponseEntity<Response<List<School>>> getSchools(
            @RequestAttribute(Request.KEY) final Request request,
            @RequestAttribute(Query.KEY) final Query query,
            @Parameter(name = MaxResults.KEY, description = MaxResults.DESCRIPTION)
            @RequestParam(required = false) final Integer maxResults,
            @Parameter(name = NextToken.KEY, description = NextToken.DESCRIPTION)
            @RequestParam(required = false) final String nextToken) throws QueryException {

        if (bucket.tryConsume(1)) {
            FilterUtil.validateFilters(School.class, query.getFilters());
            return schoolService.getSchools(request, query);
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @Operation(summary = "Retrieves a school with the given id.")
    @GetMapping(value = "/schools/{schoolId}", produces = "application/json")
    ResponseEntity<Response<List<School>>> getSchool(
            @RequestAttribute(Request.KEY) final Request request,
            @RequestAttribute(Query.KEY) final Query query,
            @PathVariable final String schoolId) throws ResourceException, QueryException {

        if (bucket.tryConsume(1)) {
            FilterUtil.validateFilters(School.class, query.getFilters());
            return schoolService.getSchool(request, schoolId);
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
