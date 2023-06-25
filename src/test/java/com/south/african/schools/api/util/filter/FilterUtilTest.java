package com.south.african.schools.api.util.filter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.south.african.schools.api.util.query.QueryException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings(
        { "checkstyle:hideutilityclassconstructor",
                "checkstyle:filetabcharacter",
                "checkstyle:missingjavadoctype",
                "checkstyle:missingJavadocmethod",
                "checkstyle:methodname"})
public class FilterUtilTest {


    @RequiredArgsConstructor
    @Getter
    @SuppressWarnings("checkstyle:javadocvariable")
    @EqualsAndHashCode
    private static final class Student {

        @Filter
        private final String name;
        @Filter
        private final String surname;
        @Filter
        private final int age;
        @Filter
        private final boolean registered;
        private final String id;
        @Filter(name = "payment")
        private final String paymentMethod;

    }

    private static final class EmptyStudent {
    }



    @Test
    public void applyFilter_whenFiltersNull() {
        Assert.assertTrue(FilterUtil.applyFilter(null, "a", "b"));
    }

    @Test
    public void applyFilter_whenFiltersEmpty() {
        Assert.assertTrue(FilterUtil.applyFilter(ImmutableMap.of(), "a", "b"));
    }

    @Test
    public void applyFilter_whenFilterPresent_withMatchingValue() {

        final Map<String, ImmutableSet<String>> filters = ImmutableMap.of(
                "a", ImmutableSet.of("1", "2", "3"),
                "b", ImmutableSet.of("1", "2", "3"));

        Assert.assertTrue(FilterUtil.applyFilter(filters, "a", "1"));
    }

    @Test
    public void applyFilter_whenFilterPresent_withoutMatchingValue() {

        final Map<String, ImmutableSet<String>> filters = ImmutableMap.of(
                "a", ImmutableSet.of("1", "2", "3"),
                "b", ImmutableSet.of("1", "2", "3"));

        Assert.assertFalse(FilterUtil.applyFilter(filters, "a", "b"));
    }

    @Test
    public void applyFilters_whenFiltersNull() throws FilterUtilException {

        Assert.assertTrue(FilterUtil.applyFilters(null,
                new Student("bob", "conner", 12, true,
                        "1", "BURSARY")));

    }

    @Test
    public void applyFilters_whenFiltersPresent_withMatchingValues() throws FilterUtilException {

        final Student student = new Student("bob", "conner", 12,
                true, "1", "BURSARY");

        Assert.assertTrue(FilterUtil.applyFilters(ImmutableMap.of(
                    "name", ImmutableSet.of("bob", "john"),
                    "surname", ImmutableSet.of("conner"),
                    "age", ImmutableSet.of("12"),
                    "registered", ImmutableSet.of("true"),
                        "payment", ImmutableSet.of("BURSARY")),
                student));


        Assert.assertTrue(
                FilterUtil.applyFilters(ImmutableMap.of(
                        "name", ImmutableSet.of("bob"),
                        "surname", ImmutableSet.of("conner"),
                        "age", ImmutableSet.of("12"),
                        "registered", ImmutableSet.of("true"),
                        "payment", ImmutableSet.of("BURSARY"),
                        "id", ImmutableSet.of("2")), // this field is not annotated as a filter.
                student));

    }

    @Test
    public void applyFilters_whenFiltersPresent_withoutMatchingValues() throws FilterUtilException {

        final Student studentA = new Student("bob", "conner", 12,
                true, "1", "BURSARY");
        final Student studentB = new Student("john", "conner", 12,
                true, "2", "BURSARY");

        Assert.assertFalse(FilterUtil.applyFilters(ImmutableMap.of(
                        "name", ImmutableSet.of("bob", "john"),
                        "surname", ImmutableSet.of("con"),
                        "age", ImmutableSet.of("12"),
                        "registered", ImmutableSet.of("true"),
                        "payment", ImmutableSet.of("CARD")),
                studentA));


        Assert.assertFalse(
                FilterUtil.applyFilters(ImmutableMap.of(
                                "name", ImmutableSet.of("bob"),
                                "surname", ImmutableSet.of("conner"),
                                "age", ImmutableSet.of("12"),
                                "registered", ImmutableSet.of("true"),
                                "payment", ImmutableSet.of("SELF"),
                                "id", ImmutableSet.of("2")), // this field is not annotated as a filter.
                        studentB));

    }

    @Test
    public void applyFilters() throws FilterUtilException {

        final Student studentA = new Student("bob", "conner", 12,
                true, "1", "SELF");
        final Student studentB = new Student("john", "conner", 10,
                true, "2", "BURSARY");
        final Student studentC = new Student("sarah", "davids", 19,
                true, "3", "BURSARY");
        final Student studentD = new Student("rey", "palmer", 19,
                true, null, "SELF");

        final ArrayList<Student> students = new ArrayList<>(
                ImmutableList.of(studentA, studentB, studentC, studentD));

        final Map<String, ImmutableSet<String>> filters = ImmutableMap.of(
                "name", ImmutableSet.of("bob", "john"),
                "surname", ImmutableSet.of("conner"),
                "age", ImmutableSet.of("12", "10"),
                "registered", ImmutableSet.of("true"),
                "payment", ImmutableSet.of("SELF", "BURSARY"),
                "id", ImmutableSet.of("2", "1"));

        FilterUtil.applyFilters(filters, students);

        Assert.assertEquals(ImmutableSet.of(studentA, studentB), new HashSet<>(students));
    }

    @Test
    public void applyFilters_withNullValues() throws FilterUtilException {

        final Student studentA = new Student("bob", "conner", 12,
                true, "1", "SELF");
        final Student studentB = new Student("john", "conner", 10,
                true, "2", "BURSARY");
        final Student studentC = new Student("", "davids", 19,
                true, "", "BURSARY");
        final Student studentD = new Student(null, "palmer", 19,
                true, null, "SELF");

        final ArrayList<Student> students = new ArrayList<>(
                ImmutableList.of(studentA, studentB, studentC, studentD));

        final Map<String, ImmutableSet<String>> filters = ImmutableMap.of(
                "registered", ImmutableSet.of("true"),
                "payment", ImmutableSet.of("SELF", "BURSARY"),
                "name", ImmutableSet.of("null", ""));

        FilterUtil.applyFilters(filters, students);

        Assert.assertEquals(ImmutableSet.of(studentC, studentD), new HashSet<>(students));
    }

    @Test
    public void getAllowedFilterNames_whenNoFiltersPresent() {

        final Set<String> allowedFilterNames = FilterUtil.getAllowedFilterKeys(EmptyStudent.class);

        Assert.assertTrue(allowedFilterNames.isEmpty());

    }

    @Test
    public void getAllowedFilterNames_whenFiltersPresent() {

        final Set<String> allowedFilterNames = FilterUtil.getAllowedFilterKeys(Student.class);

        Assert.assertEquals(ImmutableSet.of("name", "surname", "age", "registered", "payment"), allowedFilterNames);

    }

    @Test
    public void validateFilters_whenAllFiltersValid() throws QueryException {

        final ImmutableSet<String> values = ImmutableSet.of();

        assertDoesNotThrow(() -> FilterUtil.validateFilters(Student.class, ImmutableMap.of(
                "name", values,
                "surname", values,
                "age",  values,
                "registered",  values,
                "payment", values)));

    }


    @Test
    public void validateFilters_withInvalidFilters() throws QueryException {

        final ImmutableSet<String> values = ImmutableSet.of();

        final QueryException exception = assertThrows(QueryException.class,
                () -> FilterUtil.validateFilters(Student.class, ImmutableMap.of(
                "name", values,
                "surname", values,
                "age",  values,
                "registered",  values,
                "a", values)));

        Assert.assertEquals(QueryException.Type.UNKNOWN_FILTER_KEY, exception.getType());

    }


    @Test
    public void validateFilters_whenClassHasNoFilters() throws QueryException {

        final ImmutableSet<String> values = ImmutableSet.of();

        final QueryException exception = assertThrows(QueryException.class,
                () -> FilterUtil.validateFilters(EmptyStudent.class, ImmutableMap.of(
                        "name", values,
                        "surname", values,
                        "age",  values,
                        "registered",  values,
                        "a", values)));

        Assert.assertEquals(QueryException.Type.UNKNOWN_FILTER_KEY, exception.getType());

    }


}
