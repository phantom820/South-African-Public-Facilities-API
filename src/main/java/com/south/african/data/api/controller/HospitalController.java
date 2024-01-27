package com.south.african.data.api.controller;

import com.south.african.data.api.util.query.Query;
import com.south.african.data.api.util.query.QueryException;
import com.south.african.data.api.util.query.parameter.MaxResults;
import com.south.african.data.api.util.query.parameter.NextToken;
import com.south.african.data.api.util.request.Request;
import com.south.african.data.api.util.response.Response;
import com.south.african.data.api.entity.Hospital;
import com.south.african.data.api.service.HospitalService;
import com.south.african.data.api.util.filter.FilterUtil;
import com.south.african.data.api.util.resource.ResourceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for different endpoints that have a prefix of hospitals/ .
 */
@CrossOrigin(
        allowCredentials = "false",
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.GET}
)
@SuppressWarnings("unchecked")
@RestController
public class HospitalController {

    /**
     * The hospital service for obtaining data.
     */
    @Autowired
    private HospitalService hospitalService;

    @Operation(summary = "Retrieves hospitals that satisfy given query parameters.")
    @GetMapping(value = "/hospitals", produces = "application/json")
    ResponseEntity<Response<List<Hospital>>> getHospitals(
            @RequestAttribute(Request.KEY) final Request request,
            @RequestAttribute(Query.KEY) final Query query,
            @Parameter(name = MaxResults.KEY, description = MaxResults.DESCRIPTION)
            @RequestParam(required = false) Integer maxResults,
            @Parameter(name = NextToken.KEY, description = NextToken.DESCRIPTION)
            @RequestParam(required = false) String nextToken) throws QueryException {
        FilterUtil.validateFilters(Hospital.class, query.getFilters());
        return hospitalService.getHospitals(request, query);
    }

    @Operation(summary = "Retrieves a hospital with the given id.")
    @GetMapping(value = "/hospitals/{hospitalId}", produces = "application/json")
    ResponseEntity<Response<List<Hospital>>> getHospital(
            @RequestAttribute(Request.KEY) final Request request,
            @PathVariable final String hospitalId) throws ResourceException {
        return hospitalService.getHospital(request, hospitalId);
    }
}
