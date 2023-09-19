package com.south.african.schools.api.repository;


import com.south.african.schools.api.util.encoding.Pagination;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
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

    private static final String FIND_BY_IDS_QUERY_TEMPLATE = "SELECT e FROM ENTITY e WHERE e.ID_COLUMN IN :IDs";

    private static final String FIND_ALL_SORT_BY_ID_LIMIT_QUERY_TEMPLATE =
            "SELECT e FROM ENTITY e ORDER BY e.ID_COLUMN";


    private static final String FIND_ALL_AFTER_ID_SORT_BY_ID_LIMIT_QUERY_TEMPLATE =
            "SELECT e FROM ENTITY e WHERE e.ID_COLUMN > :CURSOR ORDER BY e.ID_COLUMN";

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


    /**
     * This should be used to retrieve a single record which can be uniquely identified by given column value. Otherwise
     * if multiple match the behavior is undefined.
     * @param clazz      The class for the return type.
     * @param entityName The entity name i.e Table name to run a select all qu
     * @param id         The unique identifier for the column.
     * @param idColumn   Column to check for value in.
     * @return
     * @param <T>
     * @param <ID>
     * @return  A single resource with the given id , an empty optional if no such resource.
     */
    public <T, ID> Optional<T> getById(
            final Class<T> clazz,
            final String entityName,
            final ID id,
            final String idColumn) {

        // TODO validate the id column is a unique column.
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


    /**
     * This should be used to retrieve multiple records by their ids.
     * @param clazz      The class for the return type.
     * @param entityName The entity name i.e Table name to run a select all qu
     * @param ids        The unique identifiers for the column to retrieve.
     * @param idColumn   Column to check for values in.
     * @return
     * @param <T>
     * @param <ID>
     * @return  a collection of resources with the given ids.
     */
    public <T, ID> ArrayList<T> getByIds(
            final Class<T> clazz,
            final String entityName,
            final Set<ID> ids,
            final String idColumn) {

        // TODO validate the id column is a unique column.
        final TypedQuery<T> query = entityManager.createQuery(
                        FIND_BY_IDS_QUERY_TEMPLATE.replace("ID_COLUMN", idColumn)
                                .replace("ENTITY", entityName), clazz)
                .setParameter("IDs", ids);

        return query.getResultStream().collect(Collectors.toCollection(ArrayList::new));
    }

    @SuppressWarnings("checkstyle:missingjavadocmethod")
    public <T, ID> Page<T> getPage(
            final Class<T> clazz,
            final String entityName,
            final ID previousCursor,
            final String cursorColumn,
            final int maxResults,
            final Function<T, ID> cursorExtractor) {

        // TODO some validation

        final TypedQuery<T> query;
        if (previousCursor == null) {
            query = entityManager.createQuery(
                            FIND_ALL_SORT_BY_ID_LIMIT_QUERY_TEMPLATE.replace("ID_COLUMN", cursorColumn)
                                    .replace("ENTITY", entityName), clazz)
                    .setMaxResults(maxResults + 1);

        } else {
            query = entityManager.createQuery(
                            FIND_ALL_AFTER_ID_SORT_BY_ID_LIMIT_QUERY_TEMPLATE.replace("ID_COLUMN", cursorColumn)
                                    .replace("ENTITY", entityName), clazz)
                    .setMaxResults(maxResults + 1)
                    .setParameter("CURSOR", previousCursor);
        }

        final ArrayList<T> data = query.getResultStream().collect(Collectors.toCollection(ArrayList::new));

        if (data.size() <= maxResults) {
            return new Page<>(data, null);
        }

        final ArrayList<T> subData = data.stream().limit(maxResults)
                .collect(Collectors.toCollection(ArrayList::new));
        final String cursor = cursorExtractor.apply(subData.get(subData.size() - 1)).toString();
        final String token = Pagination.createToken(cursor);
        return new Page<>(subData, token);
    }
}
