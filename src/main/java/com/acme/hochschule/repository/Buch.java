package com.acme.hochschule.repository;

import java.util.UUID;

/**
 * Record für die Bücher.
 *
 * @param id des Buchs.
 * @param isbn des Buchs.
 * @param titel des Buchs.
 * @param autor des Buchs.
 */
record Buch(UUID id, String isbn, String titel, String autor) {
}
