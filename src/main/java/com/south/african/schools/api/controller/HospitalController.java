package com.south.african.schools.api.controller;

import com.south.african.schools.api.entity.Hospital;
import com.south.african.schools.api.service.HospitalService;
import com.south.african.schools.api.util.filter.FilterUtil;
import com.south.african.schools.api.util.query.Query;
import com.south.african.schools.api.util.query.QueryException;
import com.south.african.schools.api.util.request.Request;
import com.south.african.schools.api.util.resource.ResourceException;
import com.south.african.schools.api.util.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/hospitals")
    ResponseEntity<Response<List<Hospital>>> getHospitals(
            @RequestAttribute(Request.KEY) final Request request,
            @RequestAttribute(Query.KEY) final Query query) throws QueryException {
        FilterUtil.validateFilters(Hospital.class, query.getFilters());
        return hospitalService.getHospitals(request, query);
    }

    @GetMapping("/hospitals/{hospitalId}")
    ResponseEntity<Response<List<Hospital>>> getHospital(
            @RequestAttribute(Request.KEY) final Request request,
            @PathVariable final String hospitalId) throws ResourceException {
        return hospitalService.getHospital(request, hospitalId);
    }
}
