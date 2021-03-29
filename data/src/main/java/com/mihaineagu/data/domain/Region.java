package com.mihaineagu.data.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;

import java.util.Set;

@Data
@Entity
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 1, message = "The name cannot be empty")
    @Column(name = "region_name")
    private String regionName;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private Country country;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "region")
    private Set<Location> locations = new HashSet<>();
}
