package com.south.african.schools.api.service;

import com.google.common.collect.ImmutableList;
import com.south.african.schools.api.entity.Hospital;
import com.south.african.schools.api.repository.BaseRepository;
import com.south.african.schools.api.util.query.Query;
import com.south.african.schools.api.util.request.Request;
import com.south.african.schools.api.util.resource.ResourceException;
import com.south.african.schools.api.util.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * The service for retrieving and mapping information about hospitals.
 */
@Service
public class HospitalService {

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
    public ResponseEntity<Response<List<Hospital>>> getHospitals(final Request request, final Query query) {
        final List<Hospital> hospitals = repository
                .getAll(Hospital.class, Hospital.class.getSimpleName());
        return new ResponseEntity<>(
                new Response<>(request.getId(), hospitals, null),
                HttpStatus.OK);
    }
}
