package com.south.african.schools.api.service;

import com.google.common.collect.ImmutableList;
import com.south.african.schools.api.entity.School;
import com.south.african.schools.api.repository.Page;
import com.south.african.schools.api.repository.PaginatedSchoolRepository;
import com.south.african.schools.api.util.encoding.Pagination;
import com.south.african.schools.api.util.filter.FilterUtil;
import com.south.african.schools.api.util.query.Query;
import com.south.african.schools.api.util.query.QueryException;
import com.south.african.schools.api.util.query.parameter.NextToken;
import com.south.african.schools.api.util.request.Request;
import com.south.african.schools.api.util.resource.ResourceException;
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
     * The key for school id filter.
     */
    private static final String SCHOOL_ID_FILTER = "schoolId";

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
    public ResponseEntity<Response<List<School>>> getSchool(final Request request, final String schoolId)
            throws ResourceException {

        final Optional<School> school = schoolRepository.getById(schoolId);

        if (school.isEmpty()) {
            throw ResourceException.resourceNotFound(schoolId);
        }

        return new ResponseEntity<>(
                    new Response<>(request.getId(), ImmutableList.of(school.get()), null),
                    HttpStatus.OK);
    }

    /**
     * Retrieves all schools.
     * @param request The request.
     * @param  query  The query details.
     * @return  response with a list of school
     */
    public ResponseEntity<Response<List<School>>> getSchools(final Request request, final Query query)
            throws QueryException {

        if (query.getFilters() != null && query.getFilters().containsKey(SCHOOL_ID_FILTER)) {
            final ArrayList<School> data = schoolRepository.getByIds(query.getFilters().get(SCHOOL_ID_FILTER));
            FilterUtil.applyFilters(query.getFilters(), data);
            return new ResponseEntity<>(new Response<>(request.getId(), data, null), HttpStatus.OK);
        } else if (!query.isPaginated()) {
            final ArrayList<School> data = schoolRepository.getAll();
            FilterUtil.applyFilters(query.getFilters(), data);
            return new ResponseEntity<>(new Response<>(request.getId(), data, null), HttpStatus.OK);
        }

        try {
            final Long cursor = query.hasNextToken() ? Long.parseLong(Pagination.decodeToken(query.getNextToken().value()))
                    : null;
            final Page<School> page = schoolRepository.getPage(query.getMaxResult().value(), cursor);
            FilterUtil.applyFilters(query.getFilters(), page.getData());
            return new ResponseEntity<>(new Response<>(request.getId(), page.getData(), page.getCursor()), HttpStatus.OK);
        } catch (final NumberFormatException e) {
            throw QueryException.invalidParameterValue(NextToken.KEY, query.getNextToken().value());
        }
    }
}
