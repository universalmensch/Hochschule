package com.acme.hochschule.service;

import com.acme.hochschule.entity.Person;
import com.acme.hochschule.repository.BibliothekRestRepository;
import com.acme.hochschule.repository.HochschuleRepository;
import com.acme.hochschule.repository.Leser;
import com.acme.hochschule.repository.PredicateBuilder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static org.springframework.http.HttpStatus.NOT_MODIFIED;

/**
 * Anwendungslogik zum Laden und Übertragen von Datensätzen.
 * <img src="../../../../../asciidoc/PersonReadService.svg" alt="Klassendiagramm">
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PersonReadService {
    private final HochschuleRepository repo;
    private final PredicateBuilder predicateBuilder;
    private final BibliothekRestRepository bibliothekRepository;

    /**
     * Anwendungslogik zum Lesen eines Datensatzes.
     *
     * @param id des gewünschten Datensatzes.
     *
     * @return Datensatz einer Person oder leeres Ergebnis.
     */
    public @NonNull Person findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var person = repo.findById(id)
            .orElseThrow(() -> new NotFoundException(id));
        log.debug("findById: {}", person);
        return person;
    }

    /**
     * Nach allen Personen oder nach einer mit nachname oder mit E-Mail suchen.
     *
     * @param suchkriterien Leer, E-Mail oder Nachname der gesuchten Personen.
     * @return Liste der gefundenen Personen.
     */
    @SuppressWarnings({"ReturnCount", "NestedIfDepth"})
    public @NonNull Collection<Person> find(@NonNull final Map<String, List<String>> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);

        if (suchkriterien.isEmpty()) {
            return repo.findAll();
        }

        if (suchkriterien.size() == 1) {
            final var nachnamen = suchkriterien.get("nachname");
            if (nachnamen != null && nachnamen.size() == 1) {
                final var personen = repo.findByNachname(nachnamen.get(0));
                if (personen.isEmpty()) {
                    throw new NotFoundException(suchkriterien);
                }
                log.debug("find (nachname): {}", personen);
                return personen;
            }

            final var emails = suchkriterien.get("email");
            if (emails != null && emails.size() == 1) {
                final var person = repo.findByEmail(emails.get(0));
                if (person.isEmpty()) {
                    throw new NotFoundException(suchkriterien);
                }
                final var personen = List.of(person.get());
                log.debug("find (email): {}", personen);
                return personen;
            }
        }

        final var predicate = predicateBuilder
            .build(suchkriterien)
            .orElseThrow(() -> new NotFoundException(suchkriterien));
        final var personen = repo.findAll(predicate);
        if (personen.isEmpty()) {
            throw new NotFoundException(suchkriterien);
        }
        return personen;
    }

    /**
     * Nach einem Leser suchen über den Microservice Hochschule.
     *
     * @param leserId des Lesers.
     * @return gesuchter Leser.
     */
    @SuppressWarnings("ReturnCount")
    public Leser findLeserById(final UUID leserId) {
        log.debug("findLeserById: leserId={}", leserId);

        final ResponseEntity<Leser> response;
        try {
            response = bibliothekRepository.getLeser(leserId.toString());
        } catch (final WebClientResponseException.NotFound ex) {
            log.error("findLeserById: WebClientResponseException.NotFound");
            return new Leser(null, null, null, null, null);
        } catch (final WebClientException ex) {
            log.error("findLeserById: {}", ex.getClass().getSimpleName());
            return new Leser(null, null, null, null, null);
        }

        final var statusCode = response.getStatusCode();
        log.debug("findLeserById: statusCode={}", statusCode);
        if (statusCode == NOT_MODIFIED) {
            return new Leser(null, null, null, null, null);
        }

        final var leser = response.getBody();
        log.debug("findLeserById: {}", leser);
        return leser;
    }
}
