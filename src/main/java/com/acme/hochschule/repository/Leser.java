package com.acme.hochschule.repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Record für den Leser.
 *
 * @param nachname des Lesers.
 * @param email des Lesers.
 * @param geburtsdatum des Lesers.
 * @param lesergattung des Lesers.
 * @param buecher des Lesers.
 */
public record Leser(String nachname, String email, LocalDate geburtsdatum, String lesergattung, List<Buch> buecher) {
}
