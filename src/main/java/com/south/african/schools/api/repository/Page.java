package com.south.african.schools.api.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@SuppressWarnings({
        "checkstyle:missingjavadoctype",
        "checkstyle:missingJavadocmethod",
        "checkstyle:javadocvariable"})
@RequiredArgsConstructor
@Getter
public final class Page<T> {

    private final ArrayList<T> data;
    private final String cursor;

}
