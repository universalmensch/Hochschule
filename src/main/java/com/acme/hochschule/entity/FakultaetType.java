package com.acme.hochschule.entity;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum für die Fakultät.
 */
public enum FakultaetType {

    /**
     * Fakultät für Wirtschaftsinformatik.
     */
    WIRTSCHAFTSINFORMATIK("WI"),

    /**
     * Fakultät für Informatik.
     */
    INFORMATIK("I"),

    /**
     * Fakultät für Betriebswirtschaftslehre.
     */
    BETRIEBSWIRTSCHAFTSLEHRE("BWL"),

    /**
     * Fakultät für Volkswirtschaftslehre.
     */
    VOLKSWIRTSCHAFTSLEHRE("VWL");

    private final String value;

    FakultaetType(final String value) {
        this.value = value;
    }

    /**
     * Enum Wert wird intern als String ausgegeben.
     *
     * @return interner Wert
     */
    @JsonValue
    @Override
    public String toString() {
        return value;
    }
}
