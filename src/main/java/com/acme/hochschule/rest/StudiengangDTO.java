package com.acme.hochschule.rest;

import com.acme.hochschule.entity.FakultaetType;
import com.acme.hochschule.entity.Studiengang;

/**
 * Input für den Studiengang einer Person.
 *
 * @param fakultaet der Peron.
 */
record StudiengangDTO(FakultaetType fakultaet) {
    Studiengang toStudiengang() {
        return Studiengang
            .builder()
            .fakultaet(fakultaet)
            .build();
    }
}
