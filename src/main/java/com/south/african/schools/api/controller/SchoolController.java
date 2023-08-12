package com.south.african.schools.api.controller;

import com.south.african.schools.api.entity.School;
import com.south.african.schools.api.service.SchoolService;
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
     * The school service for obtaining data.
     */
    @Autowired
    private SchoolService schoolService;

    @GetMapping("/schools")
    ResponseEntity<Response<List<School>>> getSchools(
            @RequestAttribute(Request.KEY) final Request request,
            @RequestAttribute(Query.KEY) final Query query) throws QueryException {
        FilterUtil.validateFilters(School.class, query.getFilters());
        return schoolService.getSchools(request, query);
    }

    @GetMapping("/schools/{schoolId}")
    ResponseEntity<Response<List<School>>> getSchool(
            @RequestAttribute(Request.KEY) final Request request,
            @PathVariable final String schoolId) throws ResourceException {
        return schoolService.getSchool(request, schoolId);
    }
}
