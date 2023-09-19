package com.south.african.schools.api.repository;

import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Base repository for read operations.
 */
@Repository
@SuppressWarnings("checkstyle:javadocvariable")
public class BaseRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private static final String FIND_ALL_QUERY_TEMPLATE = "SELECT e FROM ENTITY e";

    private static final String FIND_BY_ID_QUERY_TEMPLATE = "SELECT e FROM ENTITY e WHERE e.ID_COLUMN = :ID";

    /**
     * Retrieves all records from a given entity table.
     * @param clazz      The class for the return type.
     * @param entityName The entity name i.e Table name to run a select all query.
     * @return All the records from the given entity table.
     * @param <T>
     */
    public <T> ArrayList<T> getAll(final Class<T> clazz, final String entityName) {
        final TypedQuery<T> query = entityManager.createQuery(
                FIND_ALL_QUERY_TEMPLATE.replace("ENTITY", entityName), clazz);
        return query.getResultStream().collect(Collectors.toCollection(ArrayList::new));
    }


    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public <T, ID> Optional<T> getById(
            final Class<T> clazz,
            final String entityName,
            final ID id,
            final String idColumn) {

        final TypedQuery<T> query = entityManager.createQuery(
                FIND_BY_ID_QUERY_TEMPLATE.replace("ID_COLUMN", idColumn)
                        .replace("ENTITY", entityName), clazz)
                .setParameter("ID", id);

        final T result = query.getSingleResult();

        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(result);
    }
}
