package com.south.african.schools.api.repository;

import com.south.african.schools.api.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

/**
 * Data access layer for school resource, this should have read only access mechanisms.
 */
public interface SchoolRepository extends JpaRepository<School, Long> {

    /**
     * Fetches a school with the given id.
     * @param schoolId The id to search for.
     * @return An optional result.
     */
    Optional<School> findBySchoolId(String schoolId);

    /**
     * Retrieve by particular ids.
     * @param schooldIds  The school ids to search for
     * @return  School ids.
     */
    @Query(value = "SELECT * FROM SCHOOL WHERE SCHOOL_ID IN (:schoolIds)", nativeQuery = true)
    ArrayList<School> findBySchoolId(@Param("schoolIds") Set<String> schooldIds);

    /**
     * Fetches all schools.
     * @return All schools from the datastore.
     */
    ArrayList<School> findAll();

    /**
     * Fetches all schools and limits the results to the specified number.
     * @param limit The maximum number of items to return.
     * @return A list of schools that has been limited to given size.
     */
    @Query(value = "SELECT * FROM SCHOOL LIMIT :limit", nativeQuery = true)
    ArrayList<School> findAllLimit(@Param("limit") int limit);


    /**
     * Fetches all schools ordered by ID and limits the results to the specified number.
     * @param id The id.
     * @param limit The maximum number of items to return.
     * @return A list of schools that has been limited to given size.
     */
    @Query(value = "SELECT * FROM SCHOOL WHERE ID > :id ORDER BY ID LIMIT :limit", nativeQuery = true)
    ArrayList<School> findAllAfterIdSortByIdLimit(@Param("id") long id, @Param("limit") int limit);


    /**
     * Fetches all schools and returns the result sorted by id, the result is capped to be
     * no longer than the given limit.
     * @param limit  The max results to return.
     * @return  A list of schools in particular sort order.
     */
    @Query(value = "SELECT * FROM SCHOOL ORDER BY ID LIMIT :limit", nativeQuery = true)
    ArrayList<School> findAllSortByIdLimit(@Param("limit") int limit);

}
