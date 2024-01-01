package com.acme.hochschule.graphql;

import com.acme.hochschule.service.PersonReadService;
import com.acme.hochschule.entity.Person;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import static java.util.Collections.emptyMap;

/**
 * Controller-Klasse der GraphQL-Schnittstelle fü Lesezugriffe.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
class PersonQueryController {
    private final PersonReadService service;

    /**
     * Suche anhand der Personen-ID.
     *
     * @param id ID der gesuchten Person.
     * @return Die gefundene Person.
     */
    @QueryMapping
    Person person(@Argument final UUID id) {
        log.debug("person: id={}", id);
        final var person = service.findById(id);
        log.debug("person: {}", person);
        return person;
    }

    /**
     * Suche mit diversen Suchkriterien.
     *
     * @param input Suchkriterien und ihre Werte
     * @return Die gefundenen Personen
     */
    @QueryMapping
    Collection<Person> personen(@Argument final Optional<Suchkriterien> input) {
        log.debug("personen: suchkriterien={}", input);
        final var suchkriterien = input.map(Suchkriterien::toMap).orElse(emptyMap());
        final var personen = service.find(suchkriterien);
        log.debug("personen: {}", personen);
        return personen;
    }
}
