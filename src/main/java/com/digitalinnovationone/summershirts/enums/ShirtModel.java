package com.digitalinnovationone.summershirts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShirtModel {

    SPORTS("Sports"),
    TOUR("Tour"),
    PARTY("Party"),
    WORK("Work"),
    HOT_PLACES_TRAVEL("Hot place travel"),
    COLD_PLACE_TRAVEL("Cold place travel");

    private final String description;
}
