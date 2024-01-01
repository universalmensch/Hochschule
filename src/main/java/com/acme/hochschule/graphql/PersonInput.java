package com.acme.hochschule.graphql;

import com.acme.hochschule.entity.BerufType;
import com.acme.hochschule.entity.GeschlechtType;
import com.acme.hochschule.entity.Person;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Input iener Person der Grapql Schnittstelle.
 *
 * @param nachname der Person.
 * @param email der Person.
 * @param geburtsdatum der Person.
 * @param geschlecht der Person.
 * @param fakultaet der Person.
 * @param beruf der Person.
 * @param leserid der Person.
 */
record PersonInput(
    String nachname,
    String email,
    LocalDate geburtsdatum,
    GeschlechtType geschlecht,
    StudiengangInput fakultaet,
    BerufType beruf,
    UUID leserid
) {
    Person toPerson() {
        final var studiengangEntity = fakultaet() == null ? null : fakultaet().toStudiengang();

        return Person
            .builder()
            .id(null)
            .nachname(nachname)
            .email(email)
            .geburtsdatum(geburtsdatum)
            .geschlecht(geschlecht)
            .fakultaet(studiengangEntity)
            .beruf(beruf)
            .leser(leserid)
            .erzeugt(null)
            .build();
    }
}
