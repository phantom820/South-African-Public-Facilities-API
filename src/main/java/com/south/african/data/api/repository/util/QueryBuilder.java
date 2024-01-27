package com.south.african.data.api.repository.util;

import org.apache.logging.log4j.util.Strings;

import java.util.Set;

/**
 * Query builder for fetching stuff from JPQL.
 */
@SuppressWarnings({
        "checkstyle:missingjavadoctype",
        "checkstyle:missingJavadocmethod",
        "checkstyle:javadocvariable"})
public final class QueryBuilder {

    private static final String ENTITY = "$entity";
    private static final String FIELD = "$field";
    private static final String ID = "$id";
    private static final String VALUE = "$value";
    private static final String GET_ALL_QUERY_TEMPLATE = "SELECT e FROM $entity e";
    private static final String GET_ALL_WITH_FIELD_VALUE_QUERY_TEMPLATE = "SELECT e FROM $entity e WHERE e.$field = :$value";
    private static final String GET_BY_ID_QUERY_TEMPLATE = "SELECT e FROM $entity e WHERE e.$id = $value";
    private static final String GET_BY_IDS_QUERY_TEMPLATE = "SELECT e FROM $entity e WHERE e.$id IN ($value)";
    private static final String GET_ALL_SORTED_BY_ID_QUERY_TEMPLATE =
            "SELECT e FROM $entity e ORDER BY e.$id";
    private static final String GET_ALL_AFTER_ID_SORTED_BY_ID_QUERY_TEMPLATE =
            "SELECT e FROM $entity e WHERE e.$id > $value ORDER BY e.$id";

    private QueryBuilder() {}

    public static String getAll(final String entity) {
        return  GET_ALL_QUERY_TEMPLATE.replace("$entity", entity);
    }

    public static String getAllWithFieldValue(final String entity, final String field, final String value) {
        return GET_ALL_WITH_FIELD_VALUE_QUERY_TEMPLATE
                .replace(ENTITY, entity)
                .replace(FIELD, field)
                .replace(VALUE, value);
    }

    public static String getById(final String entity, final String idColumn, final String id) {
        return GET_BY_ID_QUERY_TEMPLATE
                .replace(ENTITY, entity)
                .replace(ID, idColumn)
                .replace(VALUE, id);
    }

    public static String getByIds(final String entity, final String idColumn, final Set<String> ids) {
        return GET_BY_IDS_QUERY_TEMPLATE
                .replace(ENTITY, entity)
                .replace(ID, idColumn)
                .replace(VALUE, Strings.join(ids, ','));
    }

    public static String getAllSortedById(final String entity, final String idColumn) {
        return GET_ALL_SORTED_BY_ID_QUERY_TEMPLATE
                .replace(ENTITY, entity)
                .replace(ID, idColumn);
    }

    public static String getAllAfterIdSortedById(final String entity, final String id, final String idColumn) {
        return GET_ALL_AFTER_ID_SORTED_BY_ID_QUERY_TEMPLATE
                .replace(ENTITY, entity)
                .replace(VALUE, id)
                .replace(ID, idColumn);
    }
}
