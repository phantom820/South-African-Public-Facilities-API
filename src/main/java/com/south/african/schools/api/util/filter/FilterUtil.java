package com.south.african.schools.api.util.filter;

import com.google.common.collect.ImmutableSet;
import com.south.african.schools.api.util.query.QueryParameterException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

/**
 * Filtering utility class. This is for applying filters on a resource.
 */
public final class FilterUtil {

    private FilterUtil() { }

    /**
     * For the given filter key , checks if the given value matches any allowed values and return true if it does.
     * @param filters   The filters represented as a map o.e (name -> values association).
     * @param key       The key for the specific filter to be applied.
     * @param value     A given value to check for an exact match.
     * @return true if the value has an exact match.
     */
    public static boolean applyFilter(
            final Map<String, ImmutableSet<String>> filters,
            final String key,
            final  String value) {

        if (filters == null || filters.isEmpty()) {
            return true;
        } else if (!filters.containsKey(key)) {
            return true;
        }

        for (final String allowedValue : filters.get(key)) {
            if (value.equals(allowedValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Applies the given filters on the given item. This uses reflection to inspect field members of <T> that annotated
     *  as a filter , and then checks if a field member matches any allowed value for the field.
     * @param filters   The filters represented as a map o.e (name -> values association).
     * @param thing     Object to introspect and check field member against allowed value.
     * @return true if filter predicates are satisfied.
     * @param <T>
     * @throws FilterUtilException
     */
    public static <T> boolean applyFilters(final Map<String, ImmutableSet<String>> filters, final T thing)
            throws FilterUtilException {

        if (filters == null || filters.isEmpty()) {
            return true;
        }

        try {
            final Class<?> clazz = thing.getClass();
            for (final Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Filter.class)) {
                    final String value = field.get(thing) == null ? "null" : field.get(thing).toString();
                    final String fieldName = field.getName();
                    final String filterName = field.getAnnotation(Filter.class).name();
                    final String filterKey = !filterName.equals(Filter.DEFAULT_FILTER_NAME) ? filterName : fieldName;

                    if (!applyFilter(filters, filterKey, value)) {
                        return false;
                    }
                }
            }

            return true;
        } catch (IllegalAccessException exception) {
            throw new FilterUtilException();
        }

    }

    /**
     * Filters a list of objects in place using {@link FilterUtil.class#applyFilters(Map, ArrayList)} for each object.
     * @param filters   The filters represented as a map o.e (name -> values association).
     * @param things    List of objects to filter.
     * @param <T>
     * @throws FilterUtilException
     */
    public static <T> void applyFilters(final Map<String, ImmutableSet<String>> filters, final ArrayList<T> things)
            throws FilterUtilException {
        if (filters == null || filters.isEmpty()) {
            return;
        }
        things.removeIf(t -> !applyFilters(filters, t));
    }

    /**
     * Checks for fields in a class marked as filters and collects the corresponding key for the filter to get
     * a set of which filter names that would be allowed for the class.
     * @param clazz  The class to introspect.
     * @return       A set of filter names that are allowed for T.
     * @param <T>
     */
    public static <T> ImmutableSet<String> getAllowedFilterKeys(final Class<T> clazz) {

        if (clazz == null) {
            return ImmutableSet.of();
        }

        final ImmutableSet.Builder<String> allowedFilters = new ImmutableSet.Builder<>();
        for (final Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Filter.class)) {
                final String fieldName = field.getName();
                final String filterName = field.getAnnotation(Filter.class).name();
                final String filterKey = !filterName.equals(Filter.DEFAULT_FILTER_NAME) ? filterName : fieldName;
                allowedFilters.add(filterKey);
            }
        }

        return allowedFilters.build();
    }

    /**
     * Checks if supplied filters are defined for the given resource.
     * @param clazz   The resource type to inspect for allowed filters.
     * @param filters The filters from the request.
     * @param <T>
     * @throws QueryParameterException
     */
    public static <T> void validateFilters(final Class<T> clazz, final Map<String, ImmutableSet<String>> filters)
            throws QueryParameterException {

        if (clazz == null || filters == null || filters.isEmpty()) {
            return;
        }

        final ImmutableSet<String> allowedFilterKeys = getAllowedFilterKeys(clazz);

        for (final String filterKey : filters.keySet()) {
            if (!allowedFilterKeys.contains(filterKey)) {
                throw QueryParameterException.unknownFilterKey(filterKey);
            }
        }
    }
}
