package com.south.african.schools.api.service;

import com.google.common.collect.ImmutableList;
import com.south.african.schools.api.entity.Hospital;
import com.south.african.schools.api.repository.BaseRepository;
import com.south.african.schools.api.repository.Page;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The service for retrieving and mapping information about hospitals.
 */
@Service
public class HospitalService {

    /**
     * The key for hospital id filter.
     */
    private static final String HOSPITAL_ID_FILTER = "hospitalId";

    /**
     * Underlying repository.
     */
    @Autowired
    private BaseRepository repository;

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public ResponseEntity<Response<List<Hospital>>> getHospital(final Request request, final String hospitalId)
            throws ResourceException {

        try {
            final Optional<Hospital> hospital = repository.getById(
                    Hospital.class,
                    Hospital.class.getSimpleName(),
                    Long.parseLong(hospitalId),
                    "hospitalId");

            if (hospital.isEmpty()) {
                throw ResourceException.resourceNotFound(hospitalId);
            }

            return new ResponseEntity<>(
                    new Response<>(request.getId(), ImmutableList.of(hospital.get()), null),
                    HttpStatus.OK);

        } catch (final NumberFormatException e) {
            throw ResourceException.resourceIdMalformed("hospital", hospitalId);
        }


    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public ResponseEntity<Response<List<Hospital>>> getHospitals(final Request request, final Query query)
            throws QueryException {
        if (query.getFilters() != null && query.getFilters().containsKey(HOSPITAL_ID_FILTER)) {

            final Set<Long> hospitalIds = new HashSet<>();
            for (final String hospitalId : query.getFilters().get(HOSPITAL_ID_FILTER)) {
                try {
                    hospitalIds.add(Long.parseLong(hospitalId));
                } catch (final NumberFormatException e) {
                    throw QueryException.invalidFilterValue(HOSPITAL_ID_FILTER, hospitalId);
                }
            }

            final ArrayList<Hospital> data = repository.getByIds(
                    Hospital.class,
                    Hospital.class.getSimpleName(),
                    hospitalIds,
                    HOSPITAL_ID_FILTER);

            FilterUtil.applyFilters(query.getFilters(), data);
            return new ResponseEntity<>(new Response<>(request.getId(), data, null), HttpStatus.OK);
        } else if (!query.isPaginated()) {
            final ArrayList<Hospital> data = repository.getAll(Hospital.class, Hospital.class.getSimpleName());
            FilterUtil.applyFilters(query.getFilters(), data);
            return new ResponseEntity<>(new Response<>(request.getId(), data, null), HttpStatus.OK);
        }

        try {
            final Long cursor = query.hasNextToken() ? Long.parseLong(Pagination.decodeToken(query.getNextToken().value()))
                    : null;
            final Page<Hospital> page = repository.getPage(
                    Hospital.class,
                    Hospital.class.getSimpleName(),
                    cursor,
                    HOSPITAL_ID_FILTER,
                    query.getMaxResults().value(),
                    hospital -> hospital.getHospitalId());

            FilterUtil.applyFilters(query.getFilters(), page.getData());
            return new ResponseEntity<>(new Response<>(request.getId(), page.getData(), page.getCursor()), HttpStatus.OK);
        } catch (final NumberFormatException e) {
            throw QueryException.invalidParameterValue(NextToken.KEY, query.getNextToken().value());
        }
    }
}
