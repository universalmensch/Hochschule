package com.acme.hochschule.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.acme.hochschule.entity.FakultaetType;
import lombok.Getter;

/**
 * RuntimeException, falls keine Person oder kein Studiengang gefunden wurde.
 */
@Getter
public final class NotFoundException extends RuntimeException {
    /**
     * Nicht-vorhandene ID.
     */
    private final UUID id;

    /**
     * Suchkriterien, zu denen nichts gefunden wurde.
     */
    private final Map<String, List<String>> suchkriterien;

    NotFoundException(final UUID id) {
        super("Keine Person mit der ID " + id + " gefunden.");
        this.id = id;
        suchkriterien = null;
    }

    NotFoundException(final FakultaetType fakultaet) {
        super("Kein Studiengang " + fakultaet + " gefunden.");
        id = null;
        suchkriterien = null;
    }

    NotFoundException(final Map<String, List<String>> suchkriterien) {
        super("Keine Personen gefunden.");
        id = null;
        this.suchkriterien = suchkriterien;
    }
}
