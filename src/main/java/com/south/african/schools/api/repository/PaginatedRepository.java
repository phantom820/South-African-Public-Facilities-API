package com.south.african.schools.api.repository;


import com.south.african.schools.api.entity.School;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings(
        { "checkstyle:hideutilityclassconstructor",
                "checkstyle:filetabcharacter",
                "checkstyle:missingjavadoctype",
                "checkstyle:missingJavadocmethod",
                "checkstyle:methodname"})
public interface PaginatedRepository<T, Cursor> {

    ArrayList<School> getAll();
    Page<T> getPage(int maxResults, Cursor cursor);
    Optional<T> getById(String id);
    ArrayList<T> getByIds(Set<String> id);

}
