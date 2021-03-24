package com.mihaineagu.data.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationSportId implements Serializable {

    private Long locationId;

    private Long sportId;


}
