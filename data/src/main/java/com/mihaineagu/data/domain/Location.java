package com.mihaineagu.data.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "location")
public class Location {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_name")
    private String locationName;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;
}
