package com.acme.hochschule.graphql;

import java.util.UUID;

/**
 * Value-Klasse für das Resultat, wenn an der GraphQL-Schnittstelle eine neue Person angelegt wurde.
 *
 * @param id ID der neu angelegten Person.
 */
record CreatePayload(UUID id) {
}
