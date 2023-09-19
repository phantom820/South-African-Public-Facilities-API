package com.south.african.schools.api.entity;


import com.south.african.schools.api.util.filter.Filter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Hospital entity, represents how the resource is modeled in the datastore.
 */
@Entity
@Table(name = "Hospital")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@SuppressWarnings("checkstyle:javadocvariable")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Filter
    private final Long hospitalId;

    @Filter
    private final String name;

    @Filter
    private final String category;

    @Column(length = 20)
    @Filter
    private final String province;

    @Filter
    private final String district;
}
