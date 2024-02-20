package com.south.african.data.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import com.south.african.data.api.entity.School;
import com.south.african.data.api.repository.model.Page;
import com.south.african.data.api.util.encoding.Pagination;
import com.south.african.data.api.util.query.Query;
import com.south.african.data.api.util.query.QueryException;
import com.south.african.data.api.util.query.parameter.MaxResults;
import com.south.african.data.api.util.request.Request;
import com.south.african.data.api.util.response.Response;
import com.south.african.data.api.repository.BaseRepository;
import com.south.african.data.api.util.filter.FilterUtil;
import com.south.african.data.api.util.query.parameter.NextToken;
import com.south.african.data.api.util.resource.ResourceException;
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
     * The data repository.
     */
    @Autowired
    private BaseRepository repository;

    /**
     * Retrieves a school resource with the given id.
     * @param  request The request.
     * @param schoolId The input id to use.
     * @return A list with a single school .
     */
    public ResponseEntity<Response<List<School>>> getSchool(final Request request, final String schoolId)
            throws ResourceException {

        final Optional<School> school = repository.getById(
                School.class,
                School.class.getSimpleName(),
                schoolId,
                SCHOOL_ID_FILTER);

        if (school.isEmpty()) {
            throw ResourceException.resourceNotFound(schoolId);
        }

        return new ResponseEntity<>(
                    new Response<>(request.getId(), ImmutableList.of(school.get()), null),
                    HttpStatus.OK);
    }

    /**
     * Retrieves the schools data for the given query i.e filters applied.
     * @param request The request.
     * @param  query  The query details (filters pagination etc).
     * @return  response with a list of school
     */
    public ResponseEntity<Response<List<School>>> getSchools(final Request request, final Query query)
            throws QueryException {

        if (query.getFilters() != null && query.getFilters().containsKey(SCHOOL_ID_FILTER) && query.isPaginated()) {
            throw QueryException.invalidParameterCombination(SCHOOL_ID_FILTER, MaxResults.KEY);
        } else if (query.getFilters() != null && query.getFilters().containsKey(SCHOOL_ID_FILTER)) {
            final ArrayList<School> data = repository.getByIds(
                    School.class,
                    School.class.getSimpleName(),
                    SCHOOL_ID_FILTER,
                    query.getFilters().get(SCHOOL_ID_FILTER));

            FilterUtil.applyFilters(query.getFilters(), data);
            return new ResponseEntity<>(new Response<>(request.getId(), data, null), HttpStatus.OK);
        } else if (!query.isPaginated()) {
            final ArrayList<School> data = repository.getAll(School.class, School.class.getSimpleName());
            FilterUtil.applyFilters(query.getFilters(), data);
            return new ResponseEntity<>(new Response<>(request.getId(), data, null), HttpStatus.OK);
        }

        try {
            final Long cursor = query.hasNextToken()
                    ?
                    Long.parseLong(Pagination.decodeToken(
                            query.getNextToken().value(),
                            School.class.getSimpleName())) : null;
            final Page<School> page = repository.getPage(
                    School.class,
                    School.class.getSimpleName(),
                    cursor,
                    "id",
                    query.getMaxResults().value(),
                    school -> school.getId());
            FilterUtil.applyFilters(query.getFilters(), page.getData());
            return new ResponseEntity<>(new Response<>(request.getId(), page.getData(), page.getCursor()), HttpStatus.OK);
        } catch (final NumberFormatException | JsonProcessingException e) {
            throw QueryException.invalidParameterValue(NextToken.KEY, query.getNextToken().value());
        }
    }
}
