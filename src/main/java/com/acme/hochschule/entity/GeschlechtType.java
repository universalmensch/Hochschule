package com.acme.hochschule.entity;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;
import java.util.stream.Stream;


/**
 * Enum für das Geschlecht.
 */
public enum GeschlechtType {

    /**
     * Ist männlich.
     */
    MAENNLICH("M"),

    /**
     * Ist weiblich.
     */
    WEIBLICH("W");

    private final String value;

    GeschlechtType(final String value) {
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

    /**
     * Konvertierung eines Strings in einen Enum-Wert.
     *
     * @param value Der String, zu dem ein passender Enum-Wert ermittelt werden soll.
     * @return Passender Enum-Wert oder null.
     */
    public static Optional<GeschlechtType> of(final String value) {
        return Stream.of(values())
            .filter(geschlecht -> geschlecht.value.equalsIgnoreCase(value))
            .findFirst();
    }
}
