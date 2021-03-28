package com.mihaineagu.data.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "region_name")
    private String regionName;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private Country country;

    @OneToMany(mappedBy = "region")
    private Set<Location> locations = new HashSet<>();
}
