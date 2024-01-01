package com.acme.hochschule.rest;

import com.acme.hochschule.entity.BerufType;
import com.acme.hochschule.entity.Person;
import com.acme.hochschule.entity.GeschlechtType;
import com.acme.hochschule.entity.Studiengang;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * Model-Klasse für Spring HATEOAS.
 */
@JsonPropertyOrder({
    "nachname", "email", "geburtsdatum", "geschlecht", "beruf", "fakultaet"
})
@Relation(collectionRelation = "personen", itemRelation = "person")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString(callSuper = true)
class PersonModel extends RepresentationModel<PersonModel> {
    private final String nachname;

    @EqualsAndHashCode.Include
    private final String email;

    private final LocalDate geburtsdatum;
    private final GeschlechtType geschlecht;
    private final BerufType beruf;
    private final Studiengang fakultaet;

    PersonModel(final Person person) {
        nachname = person.getNachname();
        email = person.getEmail();
        geburtsdatum = person.getGeburtsdatum();
        geschlecht = person.getGeschlecht();
        beruf = person.getBeruf();
        fakultaet = person.getFakultaet();
    }
}
