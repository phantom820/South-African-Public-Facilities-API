package com.south.african.schools.api.service;

import com.south.african.schools.api.entity.School;
import com.south.african.schools.api.repository.SchoolRepository;
import com.south.african.schools.api.util.encoding.Json;
import com.south.african.schools.api.util.filter.FilterUtil;
import com.south.african.schools.api.util.request.Request;
import com.south.african.schools.api.util.response.Response;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The service for retrieving and mapping information about schools.
 */
@Service
public class SchoolService {

    /**
     * The school data repository.
     */
    @Autowired
    private SchoolRepository schoolRepository;

    /**
     * Retrieves a school resource with the given id , otherwise a client exception if id
     * cannot be found.
     * @param schoolId The input id to use.
     * @return A list with a single school .
     */
    public ResponseEntity<Response<List<School>>> getSchool(final String schoolId) {
        final Optional<School> school = schoolRepository.findBySchoolId(schoolId);
        if (!school.isEmpty()) {
            return  new ResponseEntity<>(new Response<>("", List.of(school.get()), null), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Retrieves all schools.
     * @param request The request with query params.
     * @return  response with a list of school
     */
    public ResponseEntity<Response<List<School>>> getSchools(final Request request) throws IllegalAccessException {
        // first paginated call.
        if (request.getNextToken().isEmpty() && !request.getMaxResults().isEmpty()) {
            final ArrayList<School> schools = schoolRepository.findAllWithLimit(request.getMaxResults().getValue());
            final String nextToken = schools.isEmpty() ? null : schools.get(schools.size() - 1).getId() + "";
            FilterUtil.applyFilters(request.getFilters(), schools);
            return new ResponseEntity<>(new Response<>(request.getRequestId(), schools, nextToken), HttpStatus.OK);
        }
        // continue pagination.
        final ArrayList<School> schools = schoolRepository.findAfterWithLimit(
                Long.parseLong(request.getNextToken().getValue()), request.getMaxResults().getValue());
        final String nextToken = schools.isEmpty() ? null : schools.get(schools.size() - 1).getId() + "";
        FilterUtil.applyFilters(request.getFilters(), schools);
        return new ResponseEntity<>(new Response<>(request.getRequestId(), schools, nextToken), HttpStatus.OK);

    }


    /**
     * Retrieves all schools.
     *
     * @return  response with a list of school
     */
    public ResponseEntity<List<School>> getSchools() {
        final ArrayList<School> schools = schoolRepository.findAll();
        final List<JSONObject> response = Json.marshal(schools);
        return new ResponseEntity<>(schools, HttpStatus.OK);


    }

}
