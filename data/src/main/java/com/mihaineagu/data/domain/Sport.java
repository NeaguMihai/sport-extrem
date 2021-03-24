package com.mihaineagu.data.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sport")
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sport_type")
    private String sportType;

}
