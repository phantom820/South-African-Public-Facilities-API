package com.south.african.schools.api.repository;

import com.south.african.schools.api.entity.School;
import com.south.african.schools.api.util.encoding.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for wrapping paginated results.
 */
@Component
public final class PaginatedSchoolRepository implements PaginatedRepository<School, Long> {

    /**
     * The underlying repository.
     */
    @Autowired
    private SchoolRepository schoolRepository;


    /**
     * Fetches all schools.
     * @return All schools from the datastore.
     */
    @Override
    public ArrayList<School> getAll() {
        return  schoolRepository.findAll();
    }

    /**
     * Retrieves a page of results limited to be no more than max results page.
     * @param maxResults        The maximum number of items to return.
     * @param previousCursor    The previous cursor.
     * @return page of results.
     */
    @Override
    public Page<School> getPage(final int maxResults, final Long previousCursor) {

        final ArrayList<School> data = previousCursor == null ? schoolRepository.findAllSortByIdLimit(maxResults + 1)
                : schoolRepository.findAllAfterIdSortByIdLimit(previousCursor, maxResults + 1);

        if (data.size() <= maxResults) {
            return new Page<>(data, null);
        }

        final ArrayList<School> subData = data.stream().limit(maxResults)
                .collect(Collectors.toCollection(ArrayList::new));
        final String cursor = subData.get(subData.size() - 1).getId() + "";
        final String token = Pagination.createToken(cursor);
        return new Page<>(subData, token);
    }

    @Override
    public Optional<School> getById(final String schoolId) {
        return schoolRepository.findBySchoolId(schoolId);
    }

    @Override
    public ArrayList<School> getByIds(final Set<String> schoolIds) {
        return schoolRepository.findBySchoolId(schoolIds);
    }
}
