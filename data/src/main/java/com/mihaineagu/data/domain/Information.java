package com.mihaineagu.data.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

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

    @ManyToOne
    @MapsId("location_id")
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @MapsId("sport_id")
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "start_period")
    private Date startPeriod;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "end_period")
    private Date endPeriod;

    @Column(name = "price")
    private Integer price;

}
