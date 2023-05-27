package com.south.african.schools.api.service;

import com.google.common.collect.ImmutableList;
import com.south.african.schools.api.entity.School;
import com.south.african.schools.api.repository.Page;
import com.south.african.schools.api.repository.PaginatedSchoolRepository;
import com.south.african.schools.api.util.filter.FilterUtil;
import com.south.african.schools.api.util.request.Request;
import com.south.african.schools.api.util.response.Response;
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
    private PaginatedSchoolRepository schoolRepository;

    /**
     * Retrieves a school resource with the given id , otherwise a client exception if id
     * cannot be found.
     * @param  request The request.
     * @param schoolId The input id to use.
     * @return A list with a single school .
     */
    public ResponseEntity<Response<List<School>>> getSchool(final Request request, final String schoolId) {
        final Optional<School> school = schoolRepository.getById(schoolId);

        if (school.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                new Response<>(
                        request.getRequestId(),
                        ImmutableList.of(school.get()), null),
                        HttpStatus.OK);
    }

    /**
     * Retrieves all schools.
     * @param request The request with query params.
     * @return  response with a list of school
     */
    public ResponseEntity<Response<List<School>>> getSchools(final Request request) {

        if (request.getFilters() != null && request.getFilters().containsKey("schoolId")) {
            final ArrayList<School> data = schoolRepository.getByIds(request.getFilters().get("schoolId"));
            FilterUtil.applyFilters(request.getFilters(), data);
            return new ResponseEntity<>(new Response<>(request.getRequestId(), data, null), HttpStatus.OK);
        }

        final Long cursor = request.getNextToken().isEmpty() ? null : Long.parseLong(request.getNextToken().getValue());
        final Page<School> page = schoolRepository.getPage(request.getMaxResults().getValue(), cursor);
        FilterUtil.applyFilters(request.getFilters(), page.getData());
        return new ResponseEntity<>(new Response<>(request.getRequestId(), page.getData(), page.getCursor()), HttpStatus.OK);
    }


    /**
     * Retrieves all schools.
     *
     * @return  response with a list of school
     */
    public ResponseEntity<List<School>> getSchools() {
        final ArrayList<School> schools = new ArrayList<>();
        return new ResponseEntity<>(schools, HttpStatus.OK);
    }

}
