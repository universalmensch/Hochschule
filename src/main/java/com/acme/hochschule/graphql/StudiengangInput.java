package com.acme.hochschule.graphql;

import com.acme.hochschule.entity.FakultaetType;
import com.acme.hochschule.entity.Studiengang;

/**
 * Input für den Studiengang einer Person.
 *
 * @param fakultaet der Person.
 */
record StudiengangInput(FakultaetType fakultaet) {
    Studiengang toStudiengang() {
        return Studiengang
            .builder()
            .fakultaet(fakultaet)
            .build();
    }
}
