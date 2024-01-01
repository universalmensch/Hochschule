package com.acme.hochschule.repository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * "HTTP Interface" für den REST-Client für Kundedaten.
 */
@FunctionalInterface
@HttpExchange("/rest")
public interface BibliothekRestRepository {
    /**
     * Einen Personendatensatz anfordern.
     *
     * @param id ID des angeforderten Lesers.
     * @return Gefundener Leser
     */
    @GetExchange("/{id}")
    ResponseEntity<Leser> getLeser(@PathVariable String id);
}
