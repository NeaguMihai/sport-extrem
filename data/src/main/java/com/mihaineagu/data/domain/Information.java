package com.mihaineagu.data.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@IdClass(InformationId.class)
@Table(name = "location_sport")
public class Information {

    @Id
    @Column(name = "location_id")
    private Long locationId;

    @Id
    @Column(name = "sport_id")
    private Long sportId;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("location_id")
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("sport_id")
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @Column(name = "start_period")
    private String startPeriod;

    @Column(name = "end_period")
    private String endPeriod;

    @Column(name = "price")
    private Integer price;

}
