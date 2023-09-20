package com.south.african.data.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.south.african.data.api.util.filter.Filter;
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
 * School entity, represents how the resource is modeled in the datastore.
 */
@Entity
@Table(name = "School")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@SuppressWarnings("checkstyle:javadocvariable")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private final Long id;

     @Filter
    private final String schoolId;

    @Filter
    private final String name;

    @Filter
    private final String status;

    @Filter
    private final String sector;

    @Filter
    private final String type;

    @Filter
    private final String phase;

    @Filter
    private final String specialization;

    @Filter
    private final String examNo;

    @Column(length = 20)
    @Filter
    private final String province;

    @Filter
    private final String districtMunicipality;

    @Filter
    private final String localMunicipality;

    @Column(length = 13)
    @Filter
    private final String postalCode;

}
