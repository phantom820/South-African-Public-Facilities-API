package com.south.african.data.api.repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.south.african.data.api.repository.model.Page;
import com.south.african.data.api.repository.util.QueryBuilder;
import com.south.african.data.api.util.encoding.Pagination;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Iterator;
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

    /**
     * Retrieves all records from a given entity table.
     * @param clazz      The class for the return type.
     * @param entity     The entity name i.e Table name to run a select all query.
     * @return All the records from the given entity table.
     * @param <T>
     */
    public <T> ArrayList<T> getAll(final Class<T> clazz, final String entity) {
        final TypedQuery<T> query = entityManager.createQuery(
                QueryBuilder.getAll(entity), clazz);
        return query.getResultStream().collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     * This should be used to retrieve a single record which can be uniquely identified by given column value. Otherwise
     * if multiple match the behavior is undefined.
     * @param clazz      The class for the return type.
     * @param entity     The entity name i.e Table name to run a select all qu
     * @param id         The unique identifier for the column.
     * @param idColumn   Column to check for value in.
     * @return
     * @param <T>
     * @param <ID>
     * @return  A single resource with the given id , an empty optional if no such resource.
     */
    public <T, ID> Optional<T> getById(
            final Class<T> clazz,
            final String entity,
            final ID id,
            final String idColumn) {

        // TODO validate the id column is a unique column.
        final TypedQuery<T> query = entityManager.createQuery(
                QueryBuilder.getById(entity, idColumn, id.toString()), clazz);

        final T result = query.getSingleResult();

        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(result);
    }


    /**
     * This should be used to retrieve multiple records by their ids.
     * @param clazz      The class for the return type.
     * @param entity The entity name i.e Table name to run a select all qu
     * @param ids        The unique identifiers for the column to retrieve.
     * @param idColumn   Column to check for values in.
     * @return
     * @param <T>
     * @param <ID>
     * @return  a collection of resources with the given ids.
     */
    public <T, ID> ArrayList<T> getByIds(
            final Class<T> clazz,
            final String entity,
            final String idColumn,
            final Set<ID> ids) {

        // TODO validate the id column is a unique column.
        final Set<String> queryIds =  ids.stream()
                .map(Object::toString)
                .collect(Collectors.toSet());

        final TypedQuery<T> query = entityManager.createQuery(
                        QueryBuilder.getByIds(
                                entity,
                                idColumn,
                                queryIds),
                        clazz);

        return query.getResultStream().collect(Collectors.toCollection(ArrayList::new));
    }

    @SuppressWarnings("checkstyle:missingjavadocmethod")
    public <T, ID> Page<T> getPage(
            final Class<T> clazz,
            final String entity,
            final ID previousCursor,
            final String cursorColumn,
            final int maxResults,
            final Function<T, ID> cursorExtractor) throws JsonProcessingException {

        // TODO some validation
        final TypedQuery<T> query;

        if (previousCursor == null) {
            query = entityManager.createQuery(
                    QueryBuilder.getAllSortedById(entity, cursorColumn), clazz)
                    .setMaxResults(maxResults + 1);

        } else {
            query = entityManager.createQuery(
                    QueryBuilder.getAllAfterIdSortedById(entity, previousCursor.toString(), cursorColumn), clazz)
                    .setMaxResults(maxResults + 1);
        }

        final Iterator<T> iterator = query.getResultStream().iterator();
        final ArrayList<T> data = new ArrayList<>(maxResults);

        while (iterator.hasNext()) {
            final T thing = iterator.next();
            if (data.size() < maxResults) {
                data.add(thing);
            } else if (data.size() == maxResults) {
                final String cursor = cursorExtractor.apply(data.get(data.size() - 1)).toString();
                final String token = Pagination.createToken(cursor, entity);
                return new Page<>(data, token);
            }
        }

        return new Page<>(data, null);
    }
}
