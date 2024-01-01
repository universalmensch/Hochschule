package com.acme.hochschule.entity;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Enum für die Berufsart.
 */
public enum BerufType {

    /**
     * Ist Student.
     */
    STUDENT("S"),

    /**
     * Ist Dozent.
     */
    DOZENT("D"),

    /**
     * Ist Hausmeister.
     */
    HAUSMEISTER("H");

    private final String value;

    BerufType(final String value) {
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
    public static Optional<BerufType> of(final String value) {
        return Stream.of(values())
            .filter(beruf -> beruf.value.equalsIgnoreCase(value))
            .findFirst();
    }
}
