package com.digitalinnovationone.summershirts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShirtModel {

    TANK_TOP("Tank Top"),
    T_SHIRT("T-shirt"),
    SLEEVELESS_SHIRT("Sleeveless shirt"),
    TUBE_TOP("Tube top"),
    HAWAIIAN_SHIRT("Hawaiian shirt");

    private final String description;
}
